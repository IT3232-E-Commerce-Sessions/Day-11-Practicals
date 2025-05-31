package com.system.sampleapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.sampleapp.model.Project;
import com.system.sampleapp.repo.ProjectRepo;

@Service
public class ProjectService {
	@Autowired
	private ProjectRepo repo;
	
	public List<Project>getProject(){
		return repo.findAll();
	}
	
}
