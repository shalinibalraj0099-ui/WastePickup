package com.wastepickup.Services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.wastepickup.entity.Zone;
import com.wastepickup.repository.ZoneRepository;

@Service
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneServiceImpl(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Zone createZone(Zone zone) {
        return zoneRepository.save(zone);
    }

    @Override
    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    @Override
    public Zone getZoneById(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Zone not found with id " + id));
    }

    @Override
    public Zone updateZone(Long id, Zone zone) {
        Zone existing = getZoneById(id);
        existing.setName(zone.getName());
        return zoneRepository.save(existing);
    }

    @Override
    public void deleteZone(Long id) {
        zoneRepository.delete(getZoneById(id));
    }
}