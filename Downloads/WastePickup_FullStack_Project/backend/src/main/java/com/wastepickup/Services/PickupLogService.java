package com.wastepickup.Services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.wastepickup.entity.PickupLog;
import com.wastepickup.repository.PickupLogRepository;

@Service
public class PickupLogService {

	private final PickupLogRepository pickupLogRepository;

	public PickupLogService(PickupLogRepository pickupLogRepository) {
		this.pickupLogRepository = pickupLogRepository;
	}

	public PickupLog createPickupLog(PickupLog pickupLog) {
		validateScore(pickupLog.getScore());
		return pickupLogRepository.save(pickupLog);
	}

	public List<PickupLog> getAllPickupLogs() {
		return pickupLogRepository.findAll();
	}

	public PickupLog getPickupLogById(Long id) {
		return pickupLogRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Pickup log not found with id " + id));
	}

	public PickupLog updatePickupLog(Long id, PickupLog pickupLog) {
		validateScore(pickupLog.getScore());
		PickupLog existing = getPickupLogById(id);
		existing.setScore(pickupLog.getScore());
		existing.setPickupTime(pickupLog.getPickupTime());
		existing.setHousehold(pickupLog.getHousehold());
		return pickupLogRepository.save(existing);
	}

	public void deletePickupLog(Long id) {
		pickupLogRepository.delete(getPickupLogById(id));
	}

	private void validateScore(Integer score) {
		if (score == null || score < 0 || score > 100) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "Segregation score must be between 0 and 100");
		}
	}
}
