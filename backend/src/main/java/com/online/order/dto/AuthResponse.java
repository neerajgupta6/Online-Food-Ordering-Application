package com.online.order.dto;

public record AuthResponse(String token, Long id, String name, String email, String role) {}