package com.system.sampleapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.sampleapp.model.Project;

public interface ProjectRepo extends JpaRepository<Project, Integer> {
}