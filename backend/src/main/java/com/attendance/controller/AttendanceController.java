package com.attendance.controller;

import com.attendance.model.AttendanceRecord;
import com.attendance.repository.AttendanceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceRepository repository;

    public AttendanceController(AttendanceRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<AttendanceRecord> getAll() {
        return repository.findAllByOrderByIdAsc();
    }

    @PostMapping("/checkin")
    public ResponseEntity<?> checkIn(@RequestBody Map<String, String> body) {
        String empCode = trim(body.get("empCode"));
        String empName = trim(body.get("empName"));

        if (empCode.isEmpty() || empName.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Employee Code and Name are required"));
        }

        Optional<AttendanceRecord> active = repository.findFirstByEmpCodeAndStatusOrderByIdDesc(empCode, "IN");
        if (active.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Employee already checked in"));
        }

        AttendanceRecord record = new AttendanceRecord(empCode, empName, LocalDateTime.now(), "IN");
        AttendanceRecord saved = repository.save(record);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkOut(@RequestBody Map<String, String> body) {
        String empCode = trim(body.get("empCode"));

        if (empCode.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Employee Code is required"));
        }

        Optional<AttendanceRecord> activeOpt = repository.findFirstByEmpCodeAndStatusOrderByIdDesc(empCode, "IN");
        if (activeOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Employee has not checked in"));
        }

        AttendanceRecord record = activeOpt.get();
        record.setOutTime(LocalDateTime.now());
        record.setStatus("OUT");
        AttendanceRecord saved = repository.save(record);
        return ResponseEntity.ok(saved);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
