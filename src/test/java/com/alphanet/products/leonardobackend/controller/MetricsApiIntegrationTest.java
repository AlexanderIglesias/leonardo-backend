package com.alphanet.products.leonardobackend.controller;

import com.alphanet.products.leonardobackend.dto.CenterMetricDto;
import com.alphanet.products.leonardobackend.dto.DepartmentMetricDto;
import com.alphanet.products.leonardobackend.dto.ProgramMetricDto;
import com.alphanet.products.leonardobackend.dto.ScalarMetricDto;
import com.alphanet.products.leonardobackend.service.MetricsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MetricsApi Integration Tests")
class MetricsApiIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private MetricsService metricsService;

    @InjectMocks
    private MetricsApi metricsApi;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(metricsApi).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return scalar metrics successfully")
    void shouldReturnScalarMetricsSuccessfully() throws Exception {
        // Given
        when(metricsService.getScalarMetrics()).thenReturn(Arrays.asList(
                new ScalarMetricDto("# Aprendices inscritos únicos", 775L),
                new ScalarMetricDto("% de perfiles DEV Backend", "43.5%"),
                new ScalarMetricDto("Total centros de formación", 4L),
                new ScalarMetricDto("Promedio inglés B1-B2", "57.3%")
        ));

        // When & Then
        MvcResult result = mockMvc.perform(get("/api/v1/metrics/scalar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[0].value").exists())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("Aprendices inscritos únicos");
        assertThat(responseBody).contains("perfiles DEV Backend");
        assertThat(responseBody).contains("centros de formación");
        assertThat(responseBody).contains("inglés B1-B2");
    }

    @Test
    @DisplayName("Should return center metrics successfully")
    void shouldReturnCenterMetricsSuccessfully() throws Exception {
        // Given
        when(metricsService.getCenterMetrics()).thenReturn(List.of(
                new CenterMetricDto("SENA - Centro de Biotecnología", "Cundinamarca", 167,
                        Arrays.asList("María García", "Carlos Rodríguez"), 120, 89)
        ));

        // When & Then
        mockMvc.perform(get("/api/v1/metrics/by-center")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].centerName").exists())
                .andExpect(jsonPath("$[0].department").exists())
                .andExpect(jsonPath("$[0].totalApprentices").exists())
                .andExpect(jsonPath("$[0].instructorsRecommended").isArray())
                .andExpect(jsonPath("$[0].githubUsers").exists())
                .andExpect(jsonPath("$[0].englishB1B2").exists());
    }

    @Test
    @DisplayName("Should return program metrics successfully")
    void shouldReturnProgramMetricsSuccessfully() throws Exception {
        // Given
        when(metricsService.getProgramMetrics()).thenReturn(List.of(
                new ProgramMetricDto("SENA - Centro de Biotecnología", "Análisis y Desarrollo de Software", 85)
        ));

        // When & Then
        mockMvc.perform(get("/api/v1/metrics/by-program")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].centerName").exists())
                .andExpect(jsonPath("$[0].programName").exists())
                .andExpect(jsonPath("$[0].apprenticesCount").exists());
    }

    @Test
    @DisplayName("Should return department metrics successfully")
    void shouldReturnDepartmentMetricsSuccessfully() throws Exception {
        // Given
        when(metricsService.getDepartmentMetrics()).thenReturn(List.of(
                new DepartmentMetricDto("Cundinamarca", 167)
        ));

        // When & Then
        mockMvc.perform(get("/api/v1/metrics/by-department")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].department").exists())
                .andExpect(jsonPath("$[0].apprenticesCount").exists());
    }

    @Test
    @DisplayName("Should handle invalid endpoint gracefully")
    void shouldHandleInvalidEndpointGracefully() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/metrics/invalid-endpoint")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return valid JSON structure for scalar metrics")
    void shouldReturnValidJsonStructureForScalarMetrics() throws Exception {
        // Given
        when(metricsService.getScalarMetrics()).thenReturn(List.of(
                new ScalarMetricDto("Test Metric", 100L)
        ));

        // When & Then
        MvcResult result = mockMvc.perform(get("/api/v1/metrics/scalar"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        // Verify it's valid JSON and can be parsed
        assertThat(content).isNotEmpty();
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> objectMapper.readTree(content));
    }
}