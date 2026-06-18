package com.alba.portofolio.service;

import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.entity.Skill;

import com.alba.portofolio.repository.SkillRepository;
import com.alba.portofolio.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

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
    public Skill updateWithUser(Skill newSkill, String email) {

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow();

        Skill skill = skillRepository.findById(newSkill.getId())
                .orElseThrow();

        skill.setName(newSkill.getName());
        skill.setLevel(newSkill.getLevel());
        skill.setUser(user);

        return skillRepository.save(skill);
    }

    public List<Skill>getAll(){
        return skillRepository.findAll();
    }

    public void delete(Long id){
        skillRepository.deleteById(id);
    }
}
