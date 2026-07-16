package com.alba.portofolio.Activity.ActivityRepository;

import com.alba.portofolio.Activity.ActivityModel.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Long> {
    List<Activity> findTop5ByOrderByCreatedAtDesc();
}
