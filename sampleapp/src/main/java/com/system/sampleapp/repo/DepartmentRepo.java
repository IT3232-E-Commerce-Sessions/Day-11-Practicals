package com.system.sampleapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.sampleapp.model.Department;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Integer>{

}