package com.example.blog_app_apis.services.impl;

import com.example.blog_app_apis.models.PerspectiveResponse;
import com.example.blog_app_apis.services.ContentModerationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ContentModerationServiceImpl implements ContentModerationService {

    @Value("${perspective.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ContentModerationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public double analyzeText(String text) {

        String url = "https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key=" + apiKey;

        // Create request body
        String requestBody = String.format(
                "{ \"comment\": {\"text\": \"%s\"}, \"languages\": [\"en\"], \"requestedAttributes\": {\"TOXICITY\": {}}}",
                text
        );

        ResponseEntity<PerspectiveResponse> response = restTemplate.postForEntity(url, requestBody, PerspectiveResponse.class);

        // Extract toxicity score
        if (response.getBody() != null) {
            PerspectiveResponse.AttributeScores attributeScores = response.getBody().getAttribute("TOXICITY");
            if (attributeScores != null && attributeScores.getSummaryScore() != null) {
                return attributeScores.getSummaryScore().getValue();
            }
        }

        // Return a default score (0.0) if there was an issue
        return 0.0;
    }
}
