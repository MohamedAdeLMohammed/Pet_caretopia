package com.PetCaretopia.shared;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Component
public class SharedImageUploadService {

    @Value("${imgbb.api.key}")
    private String imgbbApiKey;

    public String uploadBase64Image(String base64Image) {
        RestTemplate restTemplate = new RestTemplate();
        String uploadUrl = "https://api.imgbb.com/1/upload?key=" + imgbbApiKey;

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("image", base64Image);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                uploadUrl,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            return (String) data.get("url");
        } else {
            throw new RuntimeException("Image upload failed");
        }
    }
    public String uploadMultipartFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(bytes);
            return uploadBase64Image(base64Image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file", e);
        }
    }

}
