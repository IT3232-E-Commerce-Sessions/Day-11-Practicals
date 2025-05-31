package com.system.sampleapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.system.sampleapp.model.Employee;
import com.system.sampleapp.repo.EmployeeRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepo repo;
	
	public List<Employee>getEmps(){
		return repo.findAll();
	}
	
	public Employee getEmp(String id) {
		if(repo.findById(id).isEmpty()) {
			throw new EntityNotFoundException("Employee Not found");
		}
		return repo.findById(id).get();
	}
	
	public String addEMp(Employee employee) {
		if(repo.findById(employee.getEmpNo()).isPresent()) {
			throw new DuplicateKeyException("The Employee id is already available");
		}
		 repo.save(employee);
		 return "New Employee added";
	}
	
	public void deleteEmp(String Empno) {
		if(repo.findById(Empno).isEmpty()) {
			throw new EntityNotFoundException("Employee Not found");
		}
		repo.deleteById(Empno);
	}
	
	public Employee updateEmpt(String EmpNo, Employee updatedEmployee) {
		if(repo.findById(EmpNo).isEmpty()) {
			throw new EntityNotFoundException("Employee Not found");
		}
		return repo.save(updatedEmployee);
	}
}
