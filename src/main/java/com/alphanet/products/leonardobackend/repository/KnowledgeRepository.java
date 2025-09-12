package com.alphanet.products.leonardobackend.repository;

import com.alphanet.products.leonardobackend.dto.projection.KnowledgeProjection;
import com.alphanet.products.leonardobackend.entity.Knowledge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {

    @Query("""
            SELECT k.id as id,
                   k.title as title,
                   CASE 
                       WHEN LENGTH(k.content) > 300 
                       THEN CONCAT(SUBSTRING(k.content, 1, 297), '...')
                       ELSE k.content 
                   END as contentSnippet,
                   k.category as category,
                   k.relevanceScore as relevanceScore
            FROM Knowledge k
            WHERE LOWER(k.title) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(k.content) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(k.category) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(k.tags) LIKE LOWER(CONCAT('%', :query, '%'))
            ORDER BY k.relevanceScore DESC NULLS LAST, k.title ASC
            """)
    Page<KnowledgeProjection> findRelevantKnowledge(@Param("query") String query, Pageable pageable);

    @Query("""
            SELECT k.id as id,
                   k.title as title,
                   CASE 
                       WHEN LENGTH(k.content) > 300 
                       THEN CONCAT(SUBSTRING(k.content, 1, 297), '...')
                       ELSE k.content 
                   END as contentSnippet,
                   k.category as category,
                   k.relevanceScore as relevanceScore
            FROM Knowledge k
            ORDER BY k.relevanceScore DESC NULLS LAST, k.title ASC
            """)
    Page<KnowledgeProjection> findAllKnowledgeProjections(Pageable pageable);

    @Query("""
            SELECT k.id as id,
                   k.title as title,
                   CASE 
                       WHEN LENGTH(k.content) > 300 
                       THEN CONCAT(SUBSTRING(k.content, 1, 297), '...')
                       ELSE k.content 
                   END as contentSnippet,
                   k.category as category,
                   k.relevanceScore as relevanceScore
            FROM Knowledge k
            WHERE LOWER(k.category) = LOWER(:category)
            ORDER BY k.relevanceScore DESC NULLS LAST, k.title ASC
            """)
    Page<KnowledgeProjection> findByCategoryProjection(@Param("category") String category, Pageable pageable);
}