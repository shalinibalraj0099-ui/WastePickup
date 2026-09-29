package com.wastepickup.Services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.wastepickup.entity.Household;
import com.wastepickup.repository.HouseholdRepository;
import com.wastepickup.repository.PickupLogRepository;

@Service
public class HouseholdServiceImpl implements HouseholdService {

    private final HouseholdRepository householdRepository;
    private final PickupLogRepository pickupLogRepository;

    public HouseholdServiceImpl(
            HouseholdRepository householdRepository,
            PickupLogRepository pickupLogRepository) {
        this.householdRepository = householdRepository;
        this.pickupLogRepository = pickupLogRepository;
    }

    @Override
    public Household createHousehold(Household household) {
        return householdRepository.save(household);
    }

    @Override
    public List<Household> getAllHouseholds() {
        return householdRepository.findAll();
    }

    @Override
    public Household getHouseholdById(Long id) {
        return householdRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Household not found with id " + id));
    }

    @Override
    public List<Household> getHouseholdsByZone(Long zoneId) {
        return householdRepository.findByZone_Id(zoneId);
    }

    @Override
    public Household updateHousehold(Long id, Household household) {
        Household existing = getHouseholdById(id);
        existing.setAddress(household.getAddress());
        existing.setPhoneNumber(household.getPhoneNumber());
        if (!household.isReminderFlag()) {
            existing.setReminderSent(false);
        }
        existing.setReminderFlag(household.isReminderFlag());
        existing.setZone(household.getZone());
        return householdRepository.save(existing);
    }

    @Override
    public void deleteHousehold(Long id) {
        householdRepository.delete(getHouseholdById(id));
    }

    @Override
    public Double calculateAverageScore(Long householdId) {
        Double average = pickupLogRepository.average(householdId);
        return average == null ? 0.0 : average;
    }

    @Override
    public List<Household> getFlaggedHouseholds() {
        return householdRepository.findByReminderFlagTrueAndReminderSentFalse();
    }

    @Override
    public Household markReminderSent(Long id) {
        Household household = getHouseholdById(id);
        if (!household.isReminderFlag()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Reminders are not enabled for this household");
        }
        if (household.getPhoneNumber() == null || household.getPhoneNumber().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "A phone number is required to mark this reminder as sent");
        }
        household.setReminderSent(true);
        return householdRepository.save(household);
    }
}