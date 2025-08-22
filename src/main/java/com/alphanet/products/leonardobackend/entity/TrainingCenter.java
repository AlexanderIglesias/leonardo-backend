package com.alphanet.products.leonardobackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "training_centers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "center_name", nullable = false)
    private String centerName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "total_apprentices")
    private Integer totalApprentices;

    @Column(name = "github_users")
    private Integer githubUsers;

    @Column(name = "english_b1_b2")
    private Integer englishB1B2;

    @OneToMany(mappedBy = "trainingCenter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Program> programs;

    @OneToMany(mappedBy = "trainingCenter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Instructor> instructors;
}
