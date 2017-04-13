package com.studentmanagement.example.controllers;

import com.studentmanagement.example.DTO.CreatingGradeDto;
import com.studentmanagement.example.DTO.GradeDto;
import com.studentmanagement.example.models.Grade;
import com.studentmanagement.example.models.Student;
import com.studentmanagement.example.services.GradeService;
import com.studentmanagement.example.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/students")
public class GradeController {
    @Autowired
    private GradeService gradeService;

    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/{studentId}/grades", method = RequestMethod.GET)
    public ResponseEntity<List<GradeDto>> getGradesByStudentId(@PathVariable("studentId") Long studentId) {
        Student student = this.studentService.getById(studentId);
        if (student == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        student.getGrades().size();
        List<Grade> grades = student.getGrades();
        grades.size();

        List<GradeDto> gradeDtos = new ArrayList<>();
        for (Grade grade : grades) {
            GradeDto gradeDto = toDto(grade);
            gradeDtos.add(gradeDto);
        }
        return new ResponseEntity<>(gradeDtos, HttpStatus.OK);
    }

    @RequestMapping(value = "/{studentId}/grades", method = RequestMethod.POST)
    public ResponseEntity<GradeDto> addGrade(@PathVariable("studentId") Long studentId, @RequestBody CreatingGradeDto dto) {
        Student student = this.studentService.getById(studentId);
        if (student == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Grade newGrade = toCreatingModel(dto);
        newGrade.setStudent(student);

        Grade savedGrade = this.gradeService.save(newGrade);

        return new ResponseEntity<>(toDto(savedGrade), HttpStatus.CREATED);
    }

    private GradeDto toDto(Grade grade) {
        GradeDto gradeDto = new GradeDto();
        gradeDto.id = grade.getId();
        gradeDto.rate = grade.getRate();
        gradeDto.description = grade.getDescription();
        return gradeDto;
    }

    private Grade toCreatingModel(CreatingGradeDto dto) {
        Grade grade = new Grade();
        grade.setRate(dto.rate);
        grade.setDescription(dto.description);
        return grade;
    }
}

