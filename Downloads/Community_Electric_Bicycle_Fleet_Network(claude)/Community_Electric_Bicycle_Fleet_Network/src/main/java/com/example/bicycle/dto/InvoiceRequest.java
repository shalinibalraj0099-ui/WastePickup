package com.example.bicycle.dto;

public record InvoiceRequest(Long salesOrderId, Long sponsorId, double amount) {}