package com.system.sampleapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.sampleapp.model.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, String> {

}
