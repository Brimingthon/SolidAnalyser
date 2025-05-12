package com.example.solidanalyzer.repository;

import com.example.solidanalyzer.entity.Metrics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricsRepository extends JpaRepository<Metrics, Long> {
}
