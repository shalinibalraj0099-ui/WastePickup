package com.example.bicycle.dto;

public record BikeRequest(String bikeCode, int batteryPercent, Double latitude, Double longitude) {}