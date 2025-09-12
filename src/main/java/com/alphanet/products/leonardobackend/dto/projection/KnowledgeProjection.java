package com.alphanet.products.leonardobackend.dto.projection;

public interface KnowledgeProjection {
    Long getId();
    String getTitle();
    String getContentSnippet(); // max 300 chars in query
    String getCategory();
    Double getRelevanceScore();
}