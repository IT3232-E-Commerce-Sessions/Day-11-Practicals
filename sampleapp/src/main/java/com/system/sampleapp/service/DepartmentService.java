package com.system.sampleapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.system.sampleapp.model.Department;
import com.system.sampleapp.repo.DepartmentRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepo repo;

    public List<Department> getDepts() {
        return repo.findAll();
    }

    public Department getDept(int id) {
		if(repo.findById(id).isEmpty()) {
			throw new EntityNotFoundException("Department not found");
		}
		return repo.findById(id).get();
	}
    
    
    public String addDept(Department department) {
        if (repo.findById(department.getId()).isPresent()) {
            throw new DuplicateKeyException("Department ID already exists: " + department.getId());
        }
        repo.save(department);
        return "New Department added";
    }

    public void deleteDept(int id) {
        Department department = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
        repo.delete(department);
    }

    public Department updateDept(int id, Department updatedDepartment) {
        Department existingDept = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));
        existingDept.setName(updatedDepartment.getName());
        return repo.save(existingDept);
    }
}