package net.skhu.tastyinventory_be.repository;

import net.skhu.tastyinventory_be.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
}
