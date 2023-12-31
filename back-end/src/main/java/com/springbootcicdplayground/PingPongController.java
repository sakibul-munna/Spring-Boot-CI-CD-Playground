package com.springbootcicdplayground;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/dummy")
public class PingPongController {

    record PingPong(String result) {
    }

    @GetMapping("/ping")
    public PingPong getPingPong() {
        return new PingPong("CD Test Success!");
    }

    @GetMapping("/rename-working")
    public String getRenameWorking() {
        return "Rename Done!";
    }
}