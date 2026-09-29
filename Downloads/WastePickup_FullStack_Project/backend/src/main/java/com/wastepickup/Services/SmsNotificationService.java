package com.wastepickup.Services;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.wastepickup.entity.Household;

@Service
public class SmsNotificationService {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${notification.twilio.account-sid:}")
    private String accountSid;

    @Value("${notification.twilio.auth-token:}")
    private String authToken;

    @Value("${notification.twilio.from-number:}")
    private String fromNumber;

    public void sendPickupReminder(Household household) {
        if (!household.isReminderFlag()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "SMS reminders are not enabled for this household");
        }
        if (household.getPhoneNumber() == null || household.getPhoneNumber().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Add an international-format phone number for this household first");
        }
        if (accountSid.isBlank() || authToken.isBlank() || fromNumber.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, "SMS delivery is not configured on the server");
        }

        String endpoint = "https://api.twilio.com/2010-04-01/Accounts/" + accountSid + "/Messages.json";
        String form = "To=" + encode(household.getPhoneNumber())
                + "&From=" + encode(fromNumber)
                + "&Body=" + encode("Waste pickup reminder: please prepare your waste for your scheduled collection.");
        String credentials = Base64.getEncoder().encodeToString(
                (accountSid + ":" + authToken).getBytes(StandardCharsets.UTF_8));
        HttpRequest request = HttpRequest.newBuilder(URI.create(endpoint))
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        try {
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY, "SMS provider rejected the reminder request");
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, "SMS request was interrupted");
        } catch (IOException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Could not connect to the SMS provider");
        }
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}