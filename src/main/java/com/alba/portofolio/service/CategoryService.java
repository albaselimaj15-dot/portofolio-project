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


    public List<Category>getAll(){
        return categoryRepository.findAll();
    }

    public void save (Category category){
        categoryRepository.save(category);
    }

    public Category getById(Long id){
        return categoryRepository.findById(id).orElse(null);
    }

    public void delete (Long id){
        categoryRepository.deleteById(id);
    }
}
