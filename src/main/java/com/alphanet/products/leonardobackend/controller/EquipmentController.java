package com.alphanet.products.leonardobackend.controller;

import com.alphanet.products.leonardobackend.dto.projection.EquipmentProjection;
import com.alphanet.products.leonardobackend.entity.Equipment;
import com.alphanet.products.leonardobackend.repository.EquipmentRepository;
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
@RequestMapping("/api/v1/equipment")
@RequiredArgsConstructor
@Tag(name = "Equipment RAG API", description = "Read-only API for equipment data optimized for GPT agent integration")
public class EquipmentController {

    private final EquipmentRepository equipmentRepository;

    @GetMapping
    @Operation(
            summary = "Get all equipment with pagination",
            description = "Returns paginated list of all equipment using optimized projections for GPT agent consumption"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Equipment list retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EquipmentProjection.class))
            )
    })
    public ResponseEntity<Page<EquipmentProjection>> getAllEquipment(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page (max 50)", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        // Limit size to prevent excessive data transfer
        size = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, size);
        Page<EquipmentProjection> equipment = equipmentRepository.findAllEquipmentProjections(pageable);
        return ResponseEntity.ok(equipment);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get equipment by ID",
            description = "Returns specific equipment details by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Equipment found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Equipment.class))
            ),
            @ApiResponse(responseCode = "404", description = "Equipment not found")
    })
    public ResponseEntity<Equipment> getEquipmentById(
            @Parameter(description = "Equipment ID", required = true)
            @PathVariable Long id) {
        
        Optional<Equipment> equipment = equipmentRepository.findById(id);
        return equipment.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/relevant")
    @Operation(
            summary = "Search relevant equipment for RAG queries",
            description = "Returns equipment matching search query using optimized projections, ordered by relevance score"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Relevant equipment found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EquipmentProjection.class))
            )
    })
    public ResponseEntity<Page<EquipmentProjection>> searchRelevantEquipment(
            @Parameter(description = "Search query term", required = true, example = "laptop")
            @RequestParam String query,
            @Parameter(description = "Maximum number of results to return", example = "10")
            @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page) {
        
        // Ensure reasonable limits for GPT agent consumption
        limit = Math.min(limit, 50);
        Pageable pageable = PageRequest.of(page, limit);
        Page<EquipmentProjection> relevantEquipment = equipmentRepository.findRelevantEquipment(query, pageable);
        return ResponseEntity.ok(relevantEquipment);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search equipment (legacy endpoint with projections)",
            description = "Legacy search endpoint updated to use projections for better performance"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Equipment search results",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EquipmentProjection.class))
            )
    })
    public ResponseEntity<Page<EquipmentProjection>> searchEquipment(
            @Parameter(description = "Search term", example = "computer")
            @RequestParam(required = false) String query,
            @Parameter(description = "Equipment type filter", example = "laptop")
            @RequestParam(required = false) String type,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        size = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, size);
        
        Page<EquipmentProjection> result;
        if (query != null && !query.trim().isEmpty()) {
            result = equipmentRepository.findRelevantEquipment(query.trim(), pageable);
        } else if (type != null && !type.trim().isEmpty()) {
            result = equipmentRepository.findByTypeProjection(type.trim(), pageable);
        } else {
            result = equipmentRepository.findAllEquipmentProjections(pageable);
        }
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/types/{type}")
    @Operation(
            summary = "Get equipment by type using projections",
            description = "Returns equipment of specific type using optimized projections"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Equipment by type retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EquipmentProjection.class))
            )
    })
    public ResponseEntity<Page<EquipmentProjection>> getEquipmentByType(
            @Parameter(description = "Equipment type", required = true, example = "laptop")
            @PathVariable String type,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        size = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, size);
        Page<EquipmentProjection> equipment = equipmentRepository.findByTypeProjection(type, pageable);
        return ResponseEntity.ok(equipment);
    }
}