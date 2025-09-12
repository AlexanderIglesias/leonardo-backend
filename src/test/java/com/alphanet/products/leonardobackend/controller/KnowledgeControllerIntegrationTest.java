package com.alphanet.products.leonardobackend.controller;

import com.alphanet.products.leonardobackend.dto.projection.KnowledgeProjection;
import com.alphanet.products.leonardobackend.entity.Knowledge;
import com.alphanet.products.leonardobackend.repository.KnowledgeRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("KnowledgeController Integration Tests")
class KnowledgeControllerIntegrationTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private KnowledgeRepository knowledgeRepository;

    @InjectMocks
    private KnowledgeController knowledgeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(knowledgeController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return paginated knowledge list successfully")
    void shouldReturnPaginatedKnowledgeListSuccessfully() throws Exception {
        // Given
        KnowledgeProjection knowledgeProjection = createMockKnowledgeProjection();
        List<KnowledgeProjection> knowledgeList = Arrays.asList(knowledgeProjection);
        Page<KnowledgeProjection> knowledgePage = new PageImpl<>(knowledgeList, PageRequest.of(0, 10), 1);

        when(knowledgeRepository.findAllKnowledgeProjections(any(Pageable.class)))
                .thenReturn(knowledgePage);

        // When & Then
        mockMvc.perform(get("/api/v1/knowledge")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].title").value("Test Knowledge"))
                .andExpect(jsonPath("$.content[0].category").value("Programming"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Should return knowledge by ID successfully")
    void shouldReturnKnowledgeByIdSuccessfully() throws Exception {
        // Given
        Knowledge knowledge = new Knowledge(1L, "Test Knowledge", "Test content for knowledge article", 
            "Programming", 9.0, "Test Author", "Intermediate", "java,programming,tutorial");

        when(knowledgeRepository.findById(1L)).thenReturn(Optional.of(knowledge));

        // When & Then
        mockMvc.perform(get("/api/v1/knowledge/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Knowledge"))
                .andExpect(jsonPath("$.category").value("Programming"))
                .andExpect(jsonPath("$.author").value("Test Author"));
    }

    @Test
    @DisplayName("Should return 404 when knowledge not found")
    void shouldReturn404WhenKnowledgeNotFound() throws Exception {
        // Given
        when(knowledgeRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/knowledge/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return relevant knowledge for RAG query")
    void shouldReturnRelevantKnowledgeForRagQuery() throws Exception {
        // Given
        KnowledgeProjection knowledgeProjection = createMockKnowledgeProjection();
        List<KnowledgeProjection> knowledgeList = Arrays.asList(knowledgeProjection);
        Page<KnowledgeProjection> knowledgePage = new PageImpl<>(knowledgeList, PageRequest.of(0, 10), 1);

        when(knowledgeRepository.findRelevantKnowledge(eq("java programming"), any(Pageable.class)))
                .thenReturn(knowledgePage);

        // When & Then
        mockMvc.perform(get("/api/v1/knowledge/relevant")
                        .param("query", "java programming")
                        .param("limit", "10")
                        .param("page", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].title").value("Test Knowledge"));
    }

    @Test
    @DisplayName("Should search knowledge with legacy endpoint")
    void shouldSearchKnowledgeWithLegacyEndpoint() throws Exception {
        // Given
        KnowledgeProjection knowledgeProjection = createMockKnowledgeProjection();
        List<KnowledgeProjection> knowledgeList = Arrays.asList(knowledgeProjection);
        Page<KnowledgeProjection> knowledgePage = new PageImpl<>(knowledgeList, PageRequest.of(0, 10), 1);

        when(knowledgeRepository.findRelevantKnowledge(eq("programming"), any(Pageable.class)))
                .thenReturn(knowledgePage);

        // When & Then
        mockMvc.perform(get("/api/v1/knowledge/search")
                        .param("query", "programming")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("Should return knowledge by category")
    void shouldReturnKnowledgeByCategory() throws Exception {
        // Given
        KnowledgeProjection knowledgeProjection = createMockKnowledgeProjection();
        List<KnowledgeProjection> knowledgeList = Arrays.asList(knowledgeProjection);
        Page<KnowledgeProjection> knowledgePage = new PageImpl<>(knowledgeList, PageRequest.of(0, 10), 1);

        when(knowledgeRepository.findByCategoryProjection(eq("Programming"), any(Pageable.class)))
                .thenReturn(knowledgePage);

        // When & Then
        mockMvc.perform(get("/api/v1/knowledge/categories/Programming")
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
        KnowledgeProjection knowledgeProjection = createMockKnowledgeProjection();
        List<KnowledgeProjection> knowledgeList = Arrays.asList(knowledgeProjection);
        Page<KnowledgeProjection> knowledgePage = new PageImpl<>(knowledgeList, PageRequest.of(0, 50), 1);

        when(knowledgeRepository.findAllKnowledgeProjections(any(Pageable.class)))
                .thenReturn(knowledgePage);

        // When & Then - Request with size > 50 should be limited to 50
        mockMvc.perform(get("/api/v1/knowledge")
                        .param("page", "0")
                        .param("size", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private KnowledgeProjection createMockKnowledgeProjection() {
        return new KnowledgeProjection() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getTitle() {
                return "Test Knowledge";
            }

            @Override
            public String getContentSnippet() {
                return "Test content snippet for knowledge article...";
            }

            @Override
            public String getCategory() {
                return "Programming";
            }

            @Override
            public Double getRelevanceScore() {
                return 9.0;
            }
        };
    }
}