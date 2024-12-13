package com.cpxHelper.myPatients.domain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<String> checkHealth() {
        // 상태 확인 로직 (예: DB 연결, 서비스 상태 등)
        return ResponseEntity.ok("OK"); // 상태가 정상일 때 "OK" 반환
    }
}

