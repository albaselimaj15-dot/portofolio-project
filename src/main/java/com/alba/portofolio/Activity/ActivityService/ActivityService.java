package com.alba.portofolio.Activity.ActivityService;

import com.alba.portofolio.Activity.ActivityModel.Activity;
import com.alba.portofolio.Activity.ActivityRepository.ActivityRepository;
import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityService(ActivityRepository activityRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    public void log(String message) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow();

        Activity activity = new Activity();
        activity.setMessage(message);
        activity.setCreatedAt(LocalDateTime.now());
        activity.setUser(user);

        activityRepository.save(activity);
    }
    public List<Activity> getLatest() {
        return activityRepository.findTop5ByOrderByCreatedAtDesc();
    }

    public long count(){
        return activityRepository.count();
    }

}