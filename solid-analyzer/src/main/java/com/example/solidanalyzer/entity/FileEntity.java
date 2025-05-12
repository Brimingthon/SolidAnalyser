package com.example.solidanalyzer.entity;

import jakarta.persistence.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class FileEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fileName;
	private LocalDateTime analysisDate;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "metrics_id")
	private Metrics metrics;

	@OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
	private List<Violation> violations = new ArrayList<>();

	public FileEntity() { }
	public FileEntity(String fileName,
	                  LocalDateTime analysisDate,
	                  Metrics metrics,
	                  List<Violation> violations) {
		this.fileName = fileName;
		this.analysisDate = analysisDate;
		this.metrics = metrics;
		this.violations = violations;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setAnalysisDate(LocalDateTime analysisDate) {
		this.analysisDate = analysisDate;
	}

	public LocalDateTime getAnalysisDate() {
		return analysisDate;
	}

	public void setMetrics(Metrics metrics) {
		this.metrics = metrics;
	}

	public Metrics getMetrics() {
		return metrics;
	}

	public void setViolations(List<Violation> violations) {
		this.violations = violations;
	}

	public List<Violation> getViolations() {
		return violations;
	}
}

