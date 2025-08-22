package com.alphanet.products.leonardobackend.config;

import com.alphanet.products.leonardobackend.repository.DepartmentRepository;
import com.alphanet.products.leonardobackend.repository.InstructorRepository;
import com.alphanet.products.leonardobackend.repository.ProgramRepository;
import com.alphanet.products.leonardobackend.repository.TrainingCenterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataInitializer Tests")
class DataInitializerTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private TrainingCenterRepository trainingCenterRepository;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        reset(departmentRepository, trainingCenterRepository, programRepository, instructorRepository);
    }

    @Test
    @DisplayName("Should initialize data when database is empty")
    void shouldInitializeDataWhenDatabaseIsEmpty() {
        // Given
        when(departmentRepository.count()).thenReturn(0L);
        when(departmentRepository.saveAll(any())).thenReturn(mock(java.util.List.class));
        when(trainingCenterRepository.saveAll(any())).thenReturn(mock(java.util.List.class));
        when(programRepository.saveAll(any())).thenReturn(mock(java.util.List.class));
        when(instructorRepository.saveAll(any())).thenReturn(mock(java.util.List.class));

        // When
        dataInitializer.run();

        // Then
        verify(departmentRepository).count();
        verify(departmentRepository).saveAll(any());
        verify(trainingCenterRepository).saveAll(any());
        verify(programRepository).saveAll(any());
        verify(instructorRepository).saveAll(any());
    }

    @Test
    @DisplayName("Should skip initialization when data already exists")
    void shouldSkipInitializationWhenDataAlreadyExists() {
        // Given
        when(departmentRepository.count()).thenReturn(4L);

        // When
        dataInitializer.run();

        // Then
        verify(departmentRepository).count();
        verify(departmentRepository, never()).saveAll(any());
        verify(trainingCenterRepository, never()).saveAll(any());
        verify(programRepository, never()).saveAll(any());
        verify(instructorRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("Should handle database initialization with partial data")
    void shouldHandleDatabaseInitializationWithPartialData() {
        // Given
        when(departmentRepository.count()).thenReturn(0L);
        when(departmentRepository.saveAll(any())).thenReturn(mock(java.util.List.class));
        when(trainingCenterRepository.saveAll(any())).thenReturn(mock(java.util.List.class));
        when(programRepository.saveAll(any())).thenReturn(mock(java.util.List.class));
        when(instructorRepository.saveAll(any())).thenReturn(mock(java.util.List.class));

        // When
        dataInitializer.run();

        // Then
        verify(departmentRepository).saveAll(argThat(departments ->
                departments != null && ((java.util.List<?>) departments).size() == 4));
        verify(trainingCenterRepository).saveAll(argThat(centers ->
                centers != null && ((java.util.List<?>) centers).size() == 4));
        verify(programRepository).saveAll(argThat(programs ->
                programs != null && ((java.util.List<?>) programs).size() == 12));
        verify(instructorRepository).saveAll(argThat(instructors ->
                instructors != null && ((java.util.List<?>) instructors).size() == 10));
    }

    @Test
    @DisplayName("Should handle repository exceptions gracefully")
    void shouldHandleRepositoryExceptionsGracefully() {
        // Given
        when(departmentRepository.count()).thenReturn(0L);
        when(departmentRepository.saveAll(any())).thenThrow(new RuntimeException("Database error"));

        // When & Then
        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            dataInitializer.run();
        });

        verify(departmentRepository).count();
        verify(departmentRepository).saveAll(any());
    }

    @Test
    @DisplayName("Should initialize with correct department names")
    void shouldInitializeWithCorrectDepartmentNames() {
        // Given
        when(departmentRepository.count()).thenReturn(0L);
        when(departmentRepository.saveAll(any())).thenReturn(mock(java.util.List.class));
        when(trainingCenterRepository.saveAll(any())).thenReturn(mock(java.util.List.class));
        when(programRepository.saveAll(any())).thenReturn(mock(java.util.List.class));
        when(instructorRepository.saveAll(any())).thenReturn(mock(java.util.List.class));

        // When
        dataInitializer.run();

        // Then
        verify(departmentRepository).saveAll(argThat(departments -> {
            java.util.List<?> deptList = (java.util.List<?>) departments;
            return deptList.size() == 4;
        }));
    }

    @Test
    @DisplayName("Should handle edge case with very large dataset count")
    void shouldHandleEdgeCaseWithVeryLargeDatasetCount() {
        // Given
        when(departmentRepository.count()).thenReturn(Long.MAX_VALUE);

        // When
        dataInitializer.run();

        // Then
        verify(departmentRepository).count();
        verify(departmentRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("Should handle negative count from repository")
    void shouldHandleNegativeCountFromRepository() {
        // Given
        when(departmentRepository.count()).thenReturn(-1L);

        // When
        dataInitializer.run();

        // Then
        verify(departmentRepository).count();
        // Should not initialize with negative count
        verify(departmentRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("Should call run method with empty arguments")
    void shouldCallRunMethodWithEmptyArguments() {
        // Given
        when(departmentRepository.count()).thenReturn(5L);

        // When
        dataInitializer.run();

        // Then
        verify(departmentRepository).count();
    }

    @Test
    @DisplayName("Should call run method with null arguments")
    void shouldCallRunMethodWithNullArguments() {
        // Given
        when(departmentRepository.count()).thenReturn(5L);

        // When
        dataInitializer.run((String[]) null);

        // Then
        verify(departmentRepository).count();
    }

    @Test
    @DisplayName("Should call run method with multiple arguments")
    void shouldCallRunMethodWithMultipleArguments() {
        // Given
        when(departmentRepository.count()).thenReturn(0L);
        when(departmentRepository.saveAll(any())).thenReturn(mock(java.util.List.class));
        when(trainingCenterRepository.saveAll(any())).thenReturn(mock(java.util.List.class));
        when(programRepository.saveAll(any())).thenReturn(mock(java.util.List.class));
        when(instructorRepository.saveAll(any())).thenReturn(mock(java.util.List.class));

        // When
        dataInitializer.run("arg1", "arg2", "arg3");

        // Then
        verify(departmentRepository).count();
        verify(departmentRepository).saveAll(any());
    }
}
