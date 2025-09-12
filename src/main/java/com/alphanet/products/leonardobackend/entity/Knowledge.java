package com.alphanet.products.leonardobackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "knowledge")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Knowledge {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String content;
    
    private String category;
    
    @Column(name = "relevance_score")
    private Double relevanceScore;
    
    private String author;
    
    @Column(name = "difficulty_level")
    private String difficultyLevel;
    
    @Column(length = 500)
    private String tags;
}