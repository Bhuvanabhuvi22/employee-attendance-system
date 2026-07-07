package com.attendance.repository;

import com.attendance.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {

    List<AttendanceRecord> findAllByOrderByIdAsc();

    // Find the most recent record for an employee that is still checked in
    Optional<AttendanceRecord> findFirstByEmpCodeAndStatusOrderByIdDesc(String empCode, String status);
}
