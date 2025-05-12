package com.example.solidanalyzer.repository;

import com.example.solidanalyzer.entity.Violation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViolationRepository extends JpaRepository<Violation, Long> {
}
