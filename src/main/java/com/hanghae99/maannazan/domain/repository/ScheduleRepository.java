package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.Schedule;
import com.hanghae99.maannazan.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByUser(User user);

    Schedule findByIdAndUser(Long scheduleId, User user);
}
