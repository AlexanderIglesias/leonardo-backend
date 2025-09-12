package com.alphanet.products.leonardobackend.controller;

import com.alphanet.products.leonardobackend.dto.projection.KnowledgeProjection;
import com.alphanet.products.leonardobackend.entity.Knowledge;
import com.alphanet.products.leonardobackend.repository.KnowledgeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/knowledge")
@RequiredArgsConstructor
@Tag(name = "Knowledge RAG API", description = "Read-only API for knowledge data optimized for GPT agent integration")
public class KnowledgeController {

    private final KnowledgeRepository knowledgeRepository;

    @GetMapping
    @Operation(
            summary = "Get all knowledge with pagination",
            description = "Returns paginated list of all knowledge using optimized projections for GPT agent consumption"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Knowledge list retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = KnowledgeProjection.class))
            )
    })
    public ResponseEntity<Page<KnowledgeProjection>> getAllKnowledge(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        // Limit size to prevent excessive data transfer
        size = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, size);
        Page<KnowledgeProjection> knowledge = knowledgeRepository.findAllKnowledgeProjections(pageable);
        return ResponseEntity.ok(knowledge);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get knowledge by ID",
            description = "Returns specific knowledge article details by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Knowledge found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Knowledge.class))
            ),
            @ApiResponse(responseCode = "404", description = "Knowledge not found")
    })
    public ResponseEntity<Knowledge> getKnowledgeById(
            @Parameter(description = "Knowledge ID", required = true)
            @PathVariable Long id) {
        
        Optional<Knowledge> knowledge = knowledgeRepository.findById(id);
        return knowledge.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/relevant")
    @Operation(
            summary = "Search relevant knowledge for RAG queries",
            description = "Returns knowledge matching search query using optimized projections, ordered by relevance score"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Relevant knowledge found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = KnowledgeProjection.class))
            )
    })
    public ResponseEntity<Page<KnowledgeProjection>> searchRelevantKnowledge(
            @Parameter(description = "Search query term", required = true, example = "java programming")
            @RequestParam String query,
            @Parameter(description = "Maximum number of results to return", example = "10")
            @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page) {
        
        // Ensure reasonable limits for GPT agent consumption
        limit = Math.min(limit, 50);
        Pageable pageable = PageRequest.of(page, limit);
        Page<KnowledgeProjection> relevantKnowledge = knowledgeRepository.findRelevantKnowledge(query, pageable);
        return ResponseEntity.ok(relevantKnowledge);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search knowledge (legacy endpoint with projections)",
            description = "Legacy search endpoint updated to use projections for better performance"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Knowledge search results",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = KnowledgeProjection.class))
            )
    })
    public ResponseEntity<Page<KnowledgeProjection>> searchKnowledge(
            @Parameter(description = "Search term", example = "programming")
            @RequestParam(required = false) String query,
            @Parameter(description = "Knowledge category filter", example = "tutorial")
            @RequestParam(required = false) String category,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        size = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, size);
        
        Page<KnowledgeProjection> result;
        if (query != null && !query.trim().isEmpty()) {
            result = knowledgeRepository.findRelevantKnowledge(query.trim(), pageable);
        } else if (category != null && !category.trim().isEmpty()) {
            result = knowledgeRepository.findByCategoryProjection(category.trim(), pageable);
        } else {
            result = knowledgeRepository.findAllKnowledgeProjections(pageable);
        }
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/categories/{category}")
    @Operation(
            summary = "Get knowledge by category using projections",
            description = "Returns knowledge of specific category using optimized projections"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Knowledge by category retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = KnowledgeProjection.class))
            )
    })
    public ResponseEntity<Page<KnowledgeProjection>> getKnowledgeByCategory(
            @Parameter(description = "Knowledge category", required = true, example = "tutorial")
            @PathVariable String category,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        size = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, size);
        Page<KnowledgeProjection> knowledge = knowledgeRepository.findByCategoryProjection(category, pageable);
        return ResponseEntity.ok(knowledge);
    }
}