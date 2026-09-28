package com.example.bicycle.dto;

public record PurchaseOrderRequest(Long vendorId, String productName, int quantity, double totalAmount) {}