package com.springbootcicdplayground.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
