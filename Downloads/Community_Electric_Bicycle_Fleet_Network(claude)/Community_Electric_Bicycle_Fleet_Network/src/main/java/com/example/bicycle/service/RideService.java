package com.example.bicycle.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.example.bicycle.model.EBike;
import com.example.bicycle.model.Ride;
import com.example.bicycle.repository.EBikeRepository;
import com.example.bicycle.repository.RideRepository;

@Service
public class RideService {
    private final RideRepository rides;
    private final EBikeRepository bikes;

    public RideService(RideRepository rides, EBikeRepository bikes) {
        this.rides = rides;
        this.bikes = bikes;
    }

    public Ride start(Long riderId, Long bikeId) {
        EBike bike = bikes.findById(bikeId).orElseThrow();
        if (bike.getBatteryPercent() < 20 || !"AVAILABLE".equals(bike.getStatus())) {
            throw new IllegalStateException("Bike unavailable");
        }
        bike.setStatus("IN_RIDE");
        bikes.save(bike);

        Ride r = new Ride();
        r.setRiderId(riderId);
        r.setBikeId(bikeId);
        r.setStartTime(LocalDateTime.now());
        r.setStatus("ACTIVE");
        return rides.save(r);
    }

    public Ride end(Long rideId) {
        Ride r = rides.findById(rideId).orElseThrow();

        LocalDateTime start = r.getStartTime() == null ? LocalDateTime.now() : r.getStartTime();
        LocalDateTime end = r.getEndTime() == null ? LocalDateTime.now() : r.getEndTime();
        r.setStartTime(start);
        r.setEndTime(end);

        long durationMinutes = Math.max(1, ChronoUnit.MINUTES.between(start, end));
        r.setDurationMinutes(durationMinutes);
        r.setFare(durationMinutes * 0.50);
        r.setStatus("COMPLETED");

        EBike b = bikes.findById(r.getBikeId()).orElseThrow();
        b.setStatus("AVAILABLE");
        bikes.save(b);
        return rides.save(r);
    }
}
