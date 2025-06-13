import os
import cv2
import numpy as np
import uuid
from PIL import Image
import json
from flask import Flask, request, jsonify
from flask_cors import CORS
from tensorflow.keras.models import load_model
from tensorflow.keras.applications.mobilenet_v2 import MobileNetV2, preprocess_input, decode_predictions
from tensorflow.keras.preprocessing import image

# Initialize Flask app
app = Flask(__name__)
CORS(app, resources={r"/predict": {"origins": "*"}})

# Constants
UPLOAD_FOLDER = 'uploads'
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg'}
BLUR_THRESHOLD = 100.0
MIN_CONFIDENCE = 0.5

# Ensure upload folder exists
os.makedirs(UPLOAD_FOLDER, exist_ok=True)

# Load models
try:
    mobilenet_model = MobileNetV2(weights='imagenet')
    custom_model = load_model('best_model.keras')
    with open('class_labels.json', 'r', encoding='utf-8') as f:
        class_labels = json.load(f)
    print(" Models loaded successfully")
except Exception as e:
    print(f" Error loading models: {e}")
    mobilenet_model = None
    custom_model = None
    class_labels = {}

def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

def check_image_quality(img_path):
    try:
        if not allowed_file(img_path):
            return False, "Invalid file format"
        img_cv = cv2.imread(img_path)
        if img_cv is None:
            return False, "Cannot process image (corrupted or unsupported format)"
        gray = cv2.cvtColor(img_cv, cv2.COLOR_BGR2GRAY)
        blur_score = cv2.Laplacian(gray, cv2.CV_64F).var()
        if blur_score < BLUR_THRESHOLD:
            return False, f"Image is too blurry (sharpness score: {blur_score:.2f})"
        return True, "Image quality passed"
    except Exception as e:
        return False, f"Image validation error: {str(e)}"

def is_cat_or_dog(predictions):
    dog_keywords = {
        'retriever', 'terrier', 'shepherd', 'poodle', 'chihuahua', 'beagle',
        'dalmatian', 'bulldog', 'husky', 'spaniel', 'greyhound', 'mastiff',
        'doberman', 'malamute', 'dog'
    }
    cat_keywords = {'cat', 'tabby', 'tiger_cat', 'persian', 'siamese'}
    for _, label, _ in predictions:
        lower_label = label.lower()
        if any(kw in lower_label for kw in dog_keywords | cat_keywords):
            return True
    return False

def preprocess_for_mobilenet(img_path):
    img = image.load_img(img_path, target_size=(224, 224))
    x = image.img_to_array(img)
    return preprocess_input(np.expand_dims(x, axis=0))

def preprocess_for_custom_model(img_path, target_size=(224, 224)):
    img = Image.open(img_path).convert('RGB')
    img = img.resize(target_size)
    img_array = np.array(img) / 255.0
    return np.expand_dims(img_array, axis=0)

@app.route('/predict', methods=['POST'])
def predict():
    if 'image' not in request.files:
        return jsonify({'error': 'No image file provided'}), 400
    file = request.files['image']
    if file.filename == '':
        return jsonify({'error': 'No file selected'}), 400
    if not allowed_file(file.filename):
        return jsonify({'error': 'Invalid file type'}), 400

    try:
        # Safe random ASCII filename to avoid Unicode issues
        extension = file.filename.rsplit('.', 1)[-1].lower()
        safe_filename = f"{uuid.uuid4().hex}.{extension}"
        filepath = os.path.join(UPLOAD_FOLDER, safe_filename)
        file.save(filepath)

        # Validate image quality
        is_valid, msg = check_image_quality(filepath)
        if not is_valid:
            os.remove(filepath)
            return jsonify({'error': msg}), 400

        # Use MobileNet to check if it's a cat or dog
        mobilenet_input = preprocess_for_mobilenet(filepath)
        mobilenet_preds = decode_predictions(mobilenet_model.predict(mobilenet_input), top=5)[0]

        if not is_cat_or_dog(mobilenet_preds):
            os.remove(filepath)
            return jsonify({
                'message': 'This is not a cat or dog.',
                'detected_objects': [{'label': label, 'probability': float(prob)} for (_, label, prob) in mobilenet_preds]
            }), 200

        # Predict breed
        custom_input = preprocess_for_custom_model(filepath)
        predictions = custom_model.predict(custom_input)[0]
        predicted_idx = int(np.argmax(predictions))
        confidence = float(predictions[predicted_idx])

        os.remove(filepath)  # Clean up

        if confidence < MIN_CONFIDENCE:
            return jsonify({
                'warning': 'Low confidence prediction',
                'breed': class_labels.get(str(predicted_idx), "unknown"),
                'confidence': confidence
            }), 200

        return jsonify({
            'breed': class_labels.get(str(predicted_idx), "unknown"),
            'confidence': confidence,
            'detected_objects': [{'label': label, 'probability': float(prob)} for (_, label, prob) in mobilenet_preds]
        })

    except Exception as e:
        if os.path.exists(filepath):
            os.remove(filepath)
        return jsonify({'error': f'Server error: {str(e)}'}), 500

if __name__ == '__main__':
    ssl_context = None
    if os.path.exists('cert.pem') and os.path.exists('key.pem'):
        ssl_context = ('cert.pem', 'key.pem')
    app.run(host='0.0.0.0', port=5000, debug=True, ssl_context=ssl_context, threaded=True)