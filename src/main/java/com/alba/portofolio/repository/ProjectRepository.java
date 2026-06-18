package com.alba.portofolio.repository;

import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.entity.Category;
import com.alba.portofolio.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByUser(AppUser user);

    List<Project> findAllByUserAndCategory(AppUser user, Category category);

    List<Project> findAllByUserAndTitleContainingIgnoreCase(AppUser user, String title);

    List<Project> findAllByCategory(Category cat);

    List<Project> findByTitleContainingIgnoreCase(String search);
}
