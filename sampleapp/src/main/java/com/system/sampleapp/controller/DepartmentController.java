package com.system.sampleapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.sampleapp.model.Department;
import com.system.sampleapp.service.DepartmentService;

@RestController
@RequestMapping("/dept")
public class DepartmentController {
    
    @Autowired
    private DepartmentService service;
    
    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return new ResponseEntity<List<Department>>(service.getDepts(), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable int id) {
    	return new ResponseEntity<Department>(service.getDept(id),HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<String> createDepartment(@RequestBody Department department) {
		return new ResponseEntity<String>(service.addDept(department), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable int id, 
            @RequestBody Department updatedDepartment) {
    	return new ResponseEntity<Department>(service.updateDept(id, updatedDepartment),HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable int id) {
        service.deleteDept(id);
        return new ResponseEntity<>("Department with ID " + id + " deleted successfully", HttpStatus.OK);
    }
}