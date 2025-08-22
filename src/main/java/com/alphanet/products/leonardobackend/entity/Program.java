package com.alphanet.products.leonardobackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "programs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "program_name", nullable = false)
    private String programName;

    @Column(name = "apprentices_count")
    private Integer apprenticesCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_center_id", nullable = false)
    private TrainingCenter trainingCenter;
}
