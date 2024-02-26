package com.trimv.base.endpoint.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("")
    public ResponseEntity<?> testException() {

        return ResponseEntity.ok("S");
    }

    @PostMapping("")
    public ResponseEntity<?> testException(@RequestBody Map<String,Object> body) {

        return ResponseEntity.ok(body);
    }
}
