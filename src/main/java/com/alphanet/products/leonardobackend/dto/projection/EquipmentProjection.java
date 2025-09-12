package com.alphanet.products.leonardobackend.dto.projection;

public interface EquipmentProjection {
    Long getId();
    String getName();
    String getType();
    String getBrand();
    String getDescription(); // max 200 chars in query
    String getPriceRange();
}