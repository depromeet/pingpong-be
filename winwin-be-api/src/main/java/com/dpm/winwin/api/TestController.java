package com.dpm.winwin.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> hello() {
        return ResponseEntity.ok(Map.of("key", "hello"));
    }
}