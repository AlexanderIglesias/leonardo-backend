package com.alphanet.products.leonardobackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String type;
    
    private String brand;
    
    @Column(length = 500)
    private String description;
    
    @Column(name = "price_range")
    private String priceRange;
    
    @Column(name = "relevance_score")
    private Double relevanceScore;
    
    private String category;
    
    @Column(name = "technical_specs", length = 1000)
    private String technicalSpecs;
}