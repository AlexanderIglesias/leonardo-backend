package com.alphanet.products.leonardobackend.repository;

import com.alphanet.products.leonardobackend.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    @Query("SELECT i.instructorName FROM Instructor i " +
            "WHERE i.trainingCenter.id = :trainingCenterId AND i.isRecommended = true")
    List<String> getRecommendedInstructorsByCenter(@Param("trainingCenterId") Long trainingCenterId);
}
