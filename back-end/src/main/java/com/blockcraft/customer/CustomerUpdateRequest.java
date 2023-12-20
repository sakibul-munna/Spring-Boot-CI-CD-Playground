package com.blockcraft.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {}
