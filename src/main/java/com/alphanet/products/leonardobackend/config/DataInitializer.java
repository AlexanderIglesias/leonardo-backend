package com.alphanet.products.leonardobackend.config;

import com.alphanet.products.leonardobackend.entity.Department;
import com.alphanet.products.leonardobackend.entity.Equipment;
import com.alphanet.products.leonardobackend.entity.Instructor;
import com.alphanet.products.leonardobackend.entity.Knowledge;
import com.alphanet.products.leonardobackend.entity.Program;
import com.alphanet.products.leonardobackend.entity.TrainingCenter;
import com.alphanet.products.leonardobackend.repository.DepartmentRepository;
import com.alphanet.products.leonardobackend.repository.EquipmentRepository;
import com.alphanet.products.leonardobackend.repository.InstructorRepository;
import com.alphanet.products.leonardobackend.repository.KnowledgeRepository;
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
    private final EquipmentRepository equipmentRepository;
    private final KnowledgeRepository knowledgeRepository;

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

        log.info("Created {} departments, {} training centers, {} programs, {} instructors, {} equipment, and {} knowledge articles",
                departments.size(), centers.size(), 12, 10, initializeEquipmentData(), initializeKnowledgeData());
    }

    private int initializeEquipmentData() {
        if (equipmentRepository.count() > 0) {
            return (int) equipmentRepository.count();
        }

        // Create sample equipment data for RAG system
        Equipment laptop1 = new Equipment(null, "Dell Latitude 5520", "Laptop", "Dell", 
            "High-performance business laptop with Intel Core i7 processor, 16GB RAM, and 512GB SSD. Perfect for software development and data analysis.", 
            "$800-$1200", 9.5, "Computers", "Intel Core i7-1165G7, 16GB DDR4, 512GB SSD, 15.6\" FHD Display");

        Equipment laptop2 = new Equipment(null, "MacBook Pro 13", "Laptop", "Apple", 
            "Compact and powerful laptop with M1 chip, ideal for creative work and programming. Features excellent battery life and retina display.", 
            "$1200-$1800", 9.8, "Computers", "Apple M1 Chip, 8GB Unified Memory, 256GB SSD, 13.3\" Retina Display");

        Equipment desktop1 = new Equipment(null, "HP EliteDesk 800 G6", "Desktop", "HP", 
            "Reliable business desktop computer suitable for office work and light development tasks. Compact design with good connectivity options.", 
            "$600-$900", 8.5, "Computers", "Intel Core i5-10500, 8GB DDR4, 256GB SSD, Windows 10 Pro");

        Equipment server1 = new Equipment(null, "Dell PowerEdge R740", "Server", "Dell", 
            "Enterprise-grade server for data center operations, cloud computing, and high-performance applications with dual processor support.", 
            "$3000-$5000", 9.2, "Servers", "Dual Intel Xeon Silver 4214, 32GB DDR4, 1TB SSD, 2U Rack");

        Equipment router1 = new Equipment(null, "Cisco Catalyst 2960", "Network Equipment", "Cisco", 
            "Managed switch with 24 Ethernet ports, ideal for small to medium business networks. Supports VLAN and quality of service features.", 
            "$300-$500", 8.8, "Networking", "24x 10/100/1000 Ports, Layer 2 Switching, VLAN Support");

        Equipment printer1 = new Equipment(null, "HP LaserJet Pro M404dn", "Printer", "HP", 
            "Monochrome laser printer perfect for office use. Fast printing speeds with automatic duplex printing capability.", 
            "$200-$350", 8.0, "Office Equipment", "38 ppm, 1200x1200 dpi, Duplex Printing, Ethernet");

        Equipment monitor1 = new Equipment(null, "Dell UltraSharp U2720Q", "Monitor", "Dell", 
            "27-inch 4K USB-C monitor with excellent color accuracy. Perfect for design work, programming, and productivity tasks.", 
            "$400-$600", 9.0, "Displays", "27\" 4K UHD (3840x2160), IPS Panel, USB-C, HDR400");

        Equipment tablet1 = new Equipment(null, "iPad Air 5th Gen", "Tablet", "Apple", 
            "Versatile tablet with M1 chip, suitable for note-taking, presentations, and mobile development testing.", 
            "$500-$800", 8.7, "Mobile Devices", "Apple M1 Chip, 10.9\" Liquid Retina Display, Touch ID, Apple Pencil Compatible");

        Equipment keyboard1 = new Equipment(null, "Logitech MX Keys", "Keyboard", "Logitech", 
            "Wireless keyboard with backlighting and multi-device connectivity. Excellent for programming and long typing sessions.", 
            "$80-$120", 8.3, "Peripherals", "Wireless, Backlit Keys, Multi-device (3), USB-C Charging");

        Equipment mouse1 = new Equipment(null, "Logitech MX Master 3", "Mouse", "Logitech", 
            "Advanced wireless mouse with precision tracking and customizable buttons. Perfect for productivity and creative work.", 
            "$60-$100", 8.6, "Peripherals", "Wireless, 4000 DPI, Hyper-fast Scrolling, USB-C Charging");

        List<Equipment> equipment = equipmentRepository.saveAll(Arrays.asList(
                laptop1, laptop2, desktop1, server1, router1, 
                printer1, monitor1, tablet1, keyboard1, mouse1
        ));

        return equipment.size();
    }

    private int initializeKnowledgeData() {
        if (knowledgeRepository.count() > 0) {
            return (int) knowledgeRepository.count();
        }

        // Create sample knowledge data for RAG system
        Knowledge java1 = new Knowledge(null, "Java Programming Fundamentals", 
            "Comprehensive guide to Java programming covering object-oriented principles, syntax, data types, control structures, and basic algorithms. This tutorial is designed for beginners who want to learn Java from scratch and build a solid foundation in programming concepts.", 
            "Programming", 9.5, "Dr. Sarah Johnson", "Beginner", "java,programming,oop,fundamentals,tutorial");

        Knowledge spring1 = new Knowledge(null, "Spring Boot REST API Development", 
            "Learn how to build scalable REST APIs using Spring Boot framework. This guide covers dependency injection, MVC architecture, data persistence with JPA, security implementation, and best practices for enterprise application development.", 
            "Web Development", 9.2, "Michael Chen", "Intermediate", "spring,spring-boot,rest-api,jpa,microservices");

        Knowledge database1 = new Knowledge(null, "Database Design and Optimization", 
            "Master the art of database design with focus on normalization, indexing strategies, query optimization, and performance tuning. Includes practical examples with PostgreSQL and MySQL databases.", 
            "Database", 8.8, "Elena Rodriguez", "Advanced", "database,sql,optimization,postgresql,mysql,indexing");

        Knowledge react1 = new Knowledge(null, "Modern React Development", 
            "Complete guide to building modern web applications with React. Covers hooks, context API, state management, routing, and integration with backend APIs. Includes best practices and performance optimization techniques.", 
            "Frontend", 9.0, "Alex Thompson", "Intermediate", "react,javascript,frontend,hooks,state-management");

        Knowledge devops1 = new Knowledge(null, "Docker and Kubernetes Essentials", 
            "Learn containerization with Docker and orchestration with Kubernetes. This practical guide covers container creation, deployment strategies, service mesh, and monitoring in cloud-native environments.", 
            "DevOps", 9.3, "David Kim", "Advanced", "docker,kubernetes,containerization,devops,cloud-native");

        Knowledge security1 = new Knowledge(null, "Web Application Security Best Practices", 
            "Comprehensive security guide covering OWASP top 10 vulnerabilities, authentication mechanisms, authorization patterns, encryption techniques, and secure coding practices for web applications.", 
            "Security", 9.1, "Lisa Wang", "Intermediate", "security,owasp,authentication,encryption,secure-coding");

        Knowledge python1 = new Knowledge(null, "Python for Data Science", 
            "Introduction to Python programming for data science applications. Covers pandas, numpy, matplotlib, data analysis techniques, and machine learning basics with scikit-learn.", 
            "Data Science", 8.9, "Robert Garcia", "Beginner", "python,data-science,pandas,numpy,machine-learning");

        Knowledge aws1 = new Knowledge(null, "AWS Cloud Architecture Patterns", 
            "Learn common cloud architecture patterns on AWS including microservices, serverless computing, auto-scaling, load balancing, and disaster recovery strategies for enterprise applications.", 
            "Cloud Computing", 9.4, "Jennifer Lee", "Advanced", "aws,cloud,architecture,serverless,microservices");

        Knowledge git1 = new Knowledge(null, "Git Version Control Mastery", 
            "Master Git version control system with advanced workflows, branching strategies, conflict resolution, and collaboration techniques. Includes GitHub and GitLab integration best practices.", 
            "Development Tools", 8.5, "Thomas Anderson", "Intermediate", "git,version-control,github,collaboration,workflows");

        Knowledge testing1 = new Knowledge(null, "Automated Testing Strategies", 
            "Comprehensive guide to automated testing including unit tests, integration tests, end-to-end testing, test-driven development (TDD), and continuous integration practices.", 
            "Testing", 8.7, "Maria Santos", "Intermediate", "testing,tdd,automation,junit,selenium,ci-cd");

        List<Knowledge> knowledge = knowledgeRepository.saveAll(Arrays.asList(
                java1, spring1, database1, react1, devops1, 
                security1, python1, aws1, git1, testing1
        ));

        return knowledge.size();
    }
}
