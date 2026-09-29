package com.wastepickup.Services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.wastepickup.entity.Schedule;
import com.wastepickup.repository.ScheduleRepository;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Schedule not found with id " + id));
    }

    @Override
    public Schedule updateSchedule(Long id, Schedule schedule) {
        Schedule existing = getScheduleById(id);
        existing.setPickupDate(schedule.getPickupDate());
        existing.setStartTime(schedule.getStartTime());
        existing.setEndTime(schedule.getEndTime());
        existing.setZone(schedule.getZone());
        return scheduleRepository.save(existing);
    }

    @Override
    public void deleteSchedule(Long id) {
        scheduleRepository.delete(getScheduleById(id));
    }
}