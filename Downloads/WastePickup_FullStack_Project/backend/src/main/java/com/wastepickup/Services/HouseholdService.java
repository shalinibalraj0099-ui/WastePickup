package com.wastepickup.Services;

import java.util.List;

import com.wastepickup.entity.Household;


public interface HouseholdService {


    Household createHousehold(Household household);



    List<Household> getAllHouseholds();



    Household getHouseholdById(Long id);



    List<Household> getHouseholdsByZone(Long zoneId);



    Household updateHousehold(Long id, Household household);



    void deleteHousehold(Long id);



    Double calculateAverageScore(Long householdId);



    List<Household> getFlaggedHouseholds();


}