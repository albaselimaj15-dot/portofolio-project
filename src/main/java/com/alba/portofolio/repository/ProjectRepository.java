package com.alba.portofolio.repository;

import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.entity.Category;
import com.alba.portofolio.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByUser(AppUser user);

    List<Project> findByCategory(Category category);

    List<Project> findByTitleContainingIgnoreCase(String title);

    List<Project> findByUserAndCategory(AppUser user, Category category);
}
