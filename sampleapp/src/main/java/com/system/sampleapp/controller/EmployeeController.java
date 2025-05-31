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
import com.system.sampleapp.model.Employee;
import com.system.sampleapp.service.EmployeeService;

@RestController
@RequestMapping("/emp")
public class EmployeeController {
	@Autowired
    private EmployeeService service;
    
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployee() {
        return new ResponseEntity<List<Employee>>(service.getEmps(), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEMployeeById(String id) {
    	return new ResponseEntity<Employee>(service.getEmp(id),HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<String> createEmployee(@RequestBody Employee employee) {
		return new ResponseEntity<String>(service.addEMp(employee), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable String id, 
            @RequestBody Employee updatedEmployee) {
    	return new ResponseEntity<Employee>(service.updateEmpt(id, updatedEmployee),HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String id) {
        service.deleteEmp(id);
        return new ResponseEntity<>("Employee with ID " + id + " deleted successfully", HttpStatus.OK);
    }
}
