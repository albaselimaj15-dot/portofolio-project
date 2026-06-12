package com.alba.portofolio.repository;

import com.alba.portofolio.entity.Project;
import com.alba.portofolio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByUser(User user);
}