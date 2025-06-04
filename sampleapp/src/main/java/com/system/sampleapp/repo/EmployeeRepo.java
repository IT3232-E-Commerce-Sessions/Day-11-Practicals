package com.system.sampleapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.sampleapp.model.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, String> {
    @Query("select e from employee e where e.salary between ?1 and ?2")
	public List<Employee>findSalaryRange(int num1,int num2);
	
	@Query("select e from employee e where e.department_dept_id like '%?1'")
	public List<Employee>getfromDeptID();
}
