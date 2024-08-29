package com.example.blog_app_apis.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class PerspectiveResponse {

    @JsonProperty("attributeScores")
    private Map<String, AttributeScores> attributeScores;

    public AttributeScores getAttribute(String key) {
        return attributeScores.get(key);
    }

    @Data
    public static class AttributeScores {
        @JsonProperty("summaryScore")
        private SummaryScore summaryScore;
    }

    @Data
    public static class SummaryScore {
        @JsonProperty("value")
        private double value;
    }
}
