package com.alphanet.products.leonardobackend.config;

import com.alphanet.products.leonardobackend.entity.Department;
import com.alphanet.products.leonardobackend.entity.Instructor;
import com.alphanet.products.leonardobackend.entity.Program;
import com.alphanet.products.leonardobackend.entity.TrainingCenter;
import com.alphanet.products.leonardobackend.repository.DepartmentRepository;
import com.alphanet.products.leonardobackend.repository.InstructorRepository;
import com.alphanet.products.leonardobackend.repository.ProgramRepository;
import com.alphanet.products.leonardobackend.repository.TrainingCenterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final TrainingCenterRepository trainingCenterRepository;
    private final ProgramRepository programRepository;
    private final InstructorRepository instructorRepository;

    @Override
    public void run(String... args) {
        if (departmentRepository.count() == 0) {
            log.info("Initializing sample data...");
            initializeData();
            log.info("Sample data initialization completed!");
        } else {
            log.info("Data already exists, skipping initialization.");
        }
    }

    private void initializeData() {
        // Create Departments
        Department cundinamarca = new Department(null, "Cundinamarca", null);
        Department bogota = new Department(null, "Bogotá D.C.", null);
        Department antioquia = new Department(null, "Antioquia", null);
        Department valle = new Department(null, "Valle del Cauca", null);

        List<Department> departments = departmentRepository.saveAll(Arrays.asList(
                cundinamarca, bogota, antioquia, valle
        ));

        // Create Training Centers
        TrainingCenter centerBiotec = new TrainingCenter(null,
                "SENA - Centro de Biotecnología Industrial",
                cundinamarca, 167, 120, 89, null, null);

        TrainingCenter centerTransporte = new TrainingCenter(null,
                "SENA - Centro de Tecnologías del Transporte",
                bogota, 245, 180, 156, null, null);

        TrainingCenter centerMedellin = new TrainingCenter(null,
                "SENA - Centro de Tecnología de la Manufactura Avanzada",
                antioquia, 198, 145, 123, null, null);

        TrainingCenter centerCali = new TrainingCenter(null,
                "SENA - Centro de Electricidad y Automatización Industrial",
                valle, 156, 98, 78, null, null);

        List<TrainingCenter> centers = trainingCenterRepository.saveAll(Arrays.asList(
                centerBiotec, centerTransporte, centerMedellin, centerCali
        ));

        // Create Programs
        Program programADSO1 = new Program(null, "Análisis y Desarrollo de Software", 85, centerBiotec);
        Program programRedes1 = new Program(null, "Gestión de Redes de Datos", 45, centerBiotec);
        Program programMantenimiento1 = new Program(null, "Mantenimiento de Equipos de Cómputo", 37, centerBiotec);

        Program programADSO2 = new Program(null, "Desarrollo de Software", 125, centerTransporte);
        Program programSistemas2 = new Program(null, "Sistemas", 78, centerTransporte);
        Program programTeleco2 = new Program(null, "Telecomunicaciones", 42, centerTransporte);

        Program programADSO3 = new Program(null, "Análisis y Desarrollo de Software", 98, centerMedellin);
        Program programAutomatizacion3 = new Program(null, "Automatización Industrial", 56, centerMedellin);
        Program programElectronica3 = new Program(null, "Electrónica", 44, centerMedellin);

        Program programADSO4 = new Program(null, "Desarrollo de Aplicaciones Web", 78, centerCali);
        Program programElectricidad4 = new Program(null, "Electricidad Industrial", 45, centerCali);
        Program programControl4 = new Program(null, "Control de Procesos", 33, centerCali);

        programRepository.saveAll(Arrays.asList(
                programADSO1, programRedes1, programMantenimiento1,
                programADSO2, programSistemas2, programTeleco2,
                programADSO3, programAutomatizacion3, programElectronica3,
                programADSO4, programElectricidad4, programControl4
        ));

        // Create Instructors
        Instructor instructor1 = new Instructor(null, "María García López", true, centerBiotec);
        Instructor instructor2 = new Instructor(null, "Carlos Andrés Rodríguez", true, centerBiotec);
        Instructor instructor3 = new Instructor(null, "Ana Patricia Hernández", false, centerBiotec);

        Instructor instructor4 = new Instructor(null, "Jorge Luis Martínez", true, centerTransporte);
        Instructor instructor5 = new Instructor(null, "Claudia Milena Torres", true, centerTransporte);
        Instructor instructor6 = new Instructor(null, "Roberto Silva Vega", false, centerTransporte);

        Instructor instructor7 = new Instructor(null, "Patricia Restrepo Gómez", true, centerMedellin);
        Instructor instructor8 = new Instructor(null, "Fernando Agudelo Mesa", true, centerMedellin);

        Instructor instructor9 = new Instructor(null, "Diana Carolina Muñoz", true, centerCali);
        Instructor instructor10 = new Instructor(null, "Andrés Felipe Vargas", false, centerCali);

        instructorRepository.saveAll(Arrays.asList(
                instructor1, instructor2, instructor3, instructor4, instructor5,
                instructor6, instructor7, instructor8, instructor9, instructor10
        ));

        log.info("Created {} departments, {} training centers, {} programs, and {} instructors",
                departments.size(), centers.size(), 12, 10);
    }
}
