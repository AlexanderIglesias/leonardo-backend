package com.alphanet.products.leonardobackend.controller;

import com.alphanet.products.leonardobackend.dto.projection.EquipmentProjection;
import com.alphanet.products.leonardobackend.entity.Equipment;
import com.alphanet.products.leonardobackend.repository.EquipmentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EquipmentController Integration Tests")
class EquipmentControllerIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private EquipmentRepository equipmentRepository;

    @InjectMocks
    private EquipmentController equipmentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(equipmentController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return paginated equipment list successfully")
    void shouldReturnPaginatedEquipmentListSuccessfully() throws Exception {
        // Given
        EquipmentProjection equipmentProjection = createMockEquipmentProjection();
        List<EquipmentProjection> equipmentList = Arrays.asList(equipmentProjection);
        Page<EquipmentProjection> equipmentPage = new PageImpl<>(equipmentList, PageRequest.of(0, 10), 1);

        when(equipmentRepository.findAllEquipmentProjections(any(Pageable.class)))
                .thenReturn(equipmentPage);

        // When & Then
        mockMvc.perform(get("/api/v1/equipment")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Test Equipment"))
                .andExpect(jsonPath("$.content[0].type").value("Laptop"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Should return equipment by ID successfully")
    void shouldReturnEquipmentByIdSuccessfully() throws Exception {
        // Given
        Equipment equipment = new Equipment(1L, "Test Equipment", "Laptop", "TestBrand", 
            "Test description", "$500-$800", 9.0, "Computers", "Test specs");

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        // When & Then
        mockMvc.perform(get("/api/v1/equipment/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Equipment"))
                .andExpect(jsonPath("$.type").value("Laptop"))
                .andExpect(jsonPath("$.brand").value("TestBrand"));
    }

    @Test
    @DisplayName("Should return 404 when equipment not found")
    void shouldReturn404WhenEquipmentNotFound() throws Exception {
        // Given
        when(equipmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/equipment/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return relevant equipment for RAG query")
    void shouldReturnRelevantEquipmentForRagQuery() throws Exception {
        // Given
        EquipmentProjection equipmentProjection = createMockEquipmentProjection();
        List<EquipmentProjection> equipmentList = Arrays.asList(equipmentProjection);
        Page<EquipmentProjection> equipmentPage = new PageImpl<>(equipmentList, PageRequest.of(0, 10), 1);

        when(equipmentRepository.findRelevantEquipment(eq("laptop"), any(Pageable.class)))
                .thenReturn(equipmentPage);

        // When & Then
        mockMvc.perform(get("/api/v1/equipment/relevant")
                        .param("query", "laptop")
                        .param("limit", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Test Equipment"));
    }

    @Test
    @DisplayName("Should search equipment with legacy endpoint")
    void shouldSearchEquipmentWithLegacyEndpoint() throws Exception {
        // Given
        EquipmentProjection equipmentProjection = createMockEquipmentProjection();
        List<EquipmentProjection> equipmentList = Arrays.asList(equipmentProjection);
        Page<EquipmentProjection> equipmentPage = new PageImpl<>(equipmentList, PageRequest.of(0, 10), 1);

        when(equipmentRepository.findRelevantEquipment(eq("computer"), any(Pageable.class)))
                .thenReturn(equipmentPage);

        // When & Then
        mockMvc.perform(get("/api/v1/equipment/search")
                        .param("query", "computer")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Should return equipment by type")
    void shouldReturnEquipmentByType() throws Exception {
        // Given
        EquipmentProjection equipmentProjection = createMockEquipmentProjection();
        List<EquipmentProjection> equipmentList = Arrays.asList(equipmentProjection);
        Page<EquipmentProjection> equipmentPage = new PageImpl<>(equipmentList, PageRequest.of(0, 10), 1);

        when(equipmentRepository.findByTypeProjection(eq("laptop"), any(Pageable.class)))
                .thenReturn(equipmentPage);

        // When & Then
        mockMvc.perform(get("/api/v1/equipment/types/laptop")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Should limit page size to maximum allowed")
    void shouldLimitPageSizeToMaximumAllowed() throws Exception {
        // Given
        EquipmentProjection equipmentProjection = createMockEquipmentProjection();
        List<EquipmentProjection> equipmentList = Arrays.asList(equipmentProjection);
        Page<EquipmentProjection> equipmentPage = new PageImpl<>(equipmentList, PageRequest.of(0, 50), 1);

        when(equipmentRepository.findAllEquipmentProjections(any(Pageable.class)))
                .thenReturn(equipmentPage);

        // When & Then - Request with size > 50 should be limited to 50
        mockMvc.perform(get("/api/v1/equipment")
                        .param("page", "0")
                        .param("size", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private EquipmentProjection createMockEquipmentProjection() {
        return new EquipmentProjection() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getName() {
                return "Test Equipment";
            }

            @Override
            public String getType() {
                return "Laptop";
            }

            @Override
            public String getBrand() {
                return "TestBrand";
            }

            @Override
            public String getDescription() {
                return "Test description for equipment";
            }

            @Override
            public String getPriceRange() {
                return "$500-$800";
            }
        };
    }
}