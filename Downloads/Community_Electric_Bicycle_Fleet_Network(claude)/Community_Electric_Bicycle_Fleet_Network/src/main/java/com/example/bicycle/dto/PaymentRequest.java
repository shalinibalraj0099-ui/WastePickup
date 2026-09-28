package com.example.bicycle.dto;

public record PaymentRequest(String referenceType, Long referenceId, double amount, String method) {}