package com.system.sampleapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.sampleapp.model.Department;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Integer>{
    @Query("select name from Department")
	public List<String>getDeptNames();
	
	@Query("select d from Department d where d.name like '%?1' ")
	public List<Department>searchname(String name);
}