package com.alba.portofolio.service;

import com.alba.portofolio.entity.Category;
import com.alba.portofolio.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }
    public void delete(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow();

        if (category.getProjects() != null && !category.getProjects().isEmpty()) {
            throw new IllegalStateException("Category cannot be deleted because it has projects");
        }

        categoryRepository.delete(category);
    }
}
