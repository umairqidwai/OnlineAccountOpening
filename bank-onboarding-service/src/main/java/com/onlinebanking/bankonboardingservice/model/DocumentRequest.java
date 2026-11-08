package com.onlinebanking.bankonboardingservice.model;

public record DocumentRequest(String fileName, String contentType, long fileSize) {
}
