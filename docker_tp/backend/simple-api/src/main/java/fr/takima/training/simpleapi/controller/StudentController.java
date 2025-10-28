package fr.takima.training.simpleapi.controller;

import fr.takima.training.simpleapi.entity.Student;
import fr.takima.training.simpleapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(value = "/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<Object> getStudents() {
        System.out.println("🚀 Version 2.0 - CI/CD automatique en action !");
        // Créer une réponse avec un message visible
        Map<String, Object> response = new HashMap<>();
        response.put("message", "✅ Déployé automatiquement via GitHub Actions - Version 2.0");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        response.put("students", studentService.getAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getStudentById(@PathVariable(name="id") long id) {
        Optional<Student> studentOptional = Optional.ofNullable(this.studentService.getStudentById(id));
        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(studentOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> addStudent(@RequestBody Student student) {
        Student savedStudent;
        try {
            savedStudent = this.studentService.addStudent(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedStudent.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Object> updateStudent(@RequestBody Student student, @PathVariable(name="id") long id) {
        Optional<Student> studentOptional = Optional.ofNullable(studentService.getStudentById(id));
        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        student.setId(id);
        this.studentService.addStudent(student);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> removeStudent(@PathVariable(name="id") long id) {
        Optional<Student> studentOptional = Optional.ofNullable(studentService.getStudentById(id));
        if (studentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        this.studentService.removeStudentById(id);

        return ResponseEntity.ok().build();
    }
}
