package com.hanghae99.maannazan.domain.repository;

import com.hanghae99.maannazan.domain.entity.Calendar;
import com.hanghae99.maannazan.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    Calendar findByUserIdAndId(Long id, Long calendarId);

    List<Calendar> findByUser(User user);
}
