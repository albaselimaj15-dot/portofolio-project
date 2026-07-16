package com.alba.portofolio.service;

import com.alba.portofolio.dto.SkillDto;
import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.entity.Skill;

import com.alba.portofolio.repository.SkillRepository;
import com.alba.portofolio.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SkillService {


    private final SkillRepository skillRepository;
    private final UserRepository userRepository;


    public SkillService(SkillRepository skillRepository, UserRepository userRepository) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    public Skill add(Skill skill){
        return skillRepository.save(skill);
    }

    public Skill update(Long id,Skill newSkill) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("Skill not found"));

        skill.setName(newSkill.getName());
        skill.setLevel(newSkill.getLevel());

        return skillRepository.save(skill);

    }
    public Skill updateWithUser(Long id, Skill dto,AppUser user) {



        Skill skill = skillRepository.findById(id)
                .orElseThrow();

        skill.setName(dto.getName());
        skill.setLevel(dto.getLevel());
        skill.setUser(user);

        return skillRepository.save(skill);
    }

    public List<Skill>getAll(){
        return skillRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id){

        System.out.println("DELETE ID: " + id);

        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        System.out.println("FOUND: " + skill.getName());

        skillRepository.delete(skill);

        System.out.println("DELETED");
    }

    public Long count() {
        return skillRepository.count();
    }

    public Skill findById(Long id){
        return skillRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
    }

    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }


}
