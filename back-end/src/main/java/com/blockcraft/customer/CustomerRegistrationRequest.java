package com.blockcraft.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
