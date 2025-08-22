package com.alphanet.products.leonardobackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instructors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instructor_name", nullable = false)
    private String instructorName;

    @Column(name = "is_recommended", nullable = false)
    private Boolean isRecommended = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_center_id", nullable = false)
    private TrainingCenter trainingCenter;
}
