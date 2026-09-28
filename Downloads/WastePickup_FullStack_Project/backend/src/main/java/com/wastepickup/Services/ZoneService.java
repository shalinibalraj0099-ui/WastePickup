package com.wastepickup.Services;

import java.util.List;

import com.wastepickup.entity.Zone;


public interface ZoneService {


    // Create a new zone
    Zone createZone(Zone zone);



    // Get all zones
    List<Zone> getAllZones();



    // Get zone by ID
    Zone getZoneById(Long id);



    // Update existing zone
    Zone updateZone(Long id, Zone zone);



    // Delete zone
    void deleteZone(Long id);



}