from flask import Flask, request, jsonify
import tensorflow as tf
from PIL import Image
import numpy as np
import json
import os

# Load model and labels
model = tf.keras.models.load_model('best_model.keras')

with open('class_labels.json', 'r') as f:
    class_labels = json.load(f)

app = Flask(__name__)

def preprocess_image(image_path, target_size=(224, 224)):
    img = Image.open(image_path).convert('RGB')
    img = img.resize(target_size)
    img_array = np.array(img) / 255.0  # Normalize
    img_array = np.expand_dims(img_array, axis=0)  # Add batch dimension
    return img_array

@app.route('/predict', methods=['POST'])
def predict():
    if 'image' not in request.files:
        return jsonify({'error': 'No image uploaded'}), 400

    image = request.files['image']

    # Check if a file was actually selected
    if image.filename == '':
        return jsonify({'error': 'No file selected'}), 400

    try:
        # Save uploaded image
        image_path = os.path.join('uploads', image.filename)
        image.save(image_path)

        # Preprocess and predict
        img_array = preprocess_image(image_path)
        prediction = model.predict(img_array)
        predicted_index = np.argmax(prediction[0])
        predicted_label = class_labels[str(predicted_index)]

        return jsonify({
            'breed': predicted_label,
            'confidence': float(np.max(prediction[0]))
        })
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    os.makedirs('uploads', exist_ok=True)
    app.run(
            debug=True,
            host='0.0.0.0',
            port=5000,
            ssl_context=('cert.pem', 'key.pem')
        )
