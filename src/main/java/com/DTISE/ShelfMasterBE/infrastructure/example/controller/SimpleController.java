package com.DTISE.ShelfMasterBE.infrastructure.example.controller;

import com.DTISE.ShelfMasterBE.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/simple")
public class SimpleController {
    @GetMapping()
    public ResponseEntity<?> test() {
        return ApiResponse.successfulResponse("Hello, World!", null);
    }
}
