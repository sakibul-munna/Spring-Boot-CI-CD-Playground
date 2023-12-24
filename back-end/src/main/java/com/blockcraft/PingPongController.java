package com.blockcraft;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/dummy")
public class PingPongController {

    record PingPong(String result) {
    }

    @GetMapping("/ping")
    public PingPong getPingPong() {
        return new PingPong("CD Test Success!");
    }
}