package com.studentmanagement.example.controllers;

import com.studentmanagement.example.DTO.CreatingStudentDto;
import com.studentmanagement.example.DTO.StudentDto;
import com.studentmanagement.example.models.Student;
import com.studentmanagement.example.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/students")
public class StudentController {
    @Autowired
    private StudentService service;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<StudentDto>> get() {
        List<Student> students = this.service.getAll();
        if (students.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<StudentDto> result = new ArrayList<>();

        for (Student student : students) {
            StudentDto dto = toDto(student);
            result.add(dto);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<StudentDto> addStudent(@RequestBody CreatingStudentDto studentDto) {
        Student student = toCreatingModel(studentDto);
        Student savedStudent = this.service.save(student);
        return new ResponseEntity<>(toDto(savedStudent), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") Long id) {
        Student student = this.service.getById(id);
        if (student == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(toDto(student), HttpStatus.OK);
    }

    private StudentDto toDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.firstName = student.getFirstName();
        dto.lastName = student.getLastName();
        dto.id = student.getId();
        return dto;
    }

    private Student toCreatingModel(CreatingStudentDto dto) {
        Student student = new Student();
        student.setFirstName(dto.firstName);
        student.setLastName(dto.lastName);
        return student;
    }
}
