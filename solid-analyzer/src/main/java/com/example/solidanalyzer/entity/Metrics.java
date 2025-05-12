package com.example.solidanalyzer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity

public class Metrics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int classCount;
	private int methodCount;
	private int cyclomaticComplexity;
	private int cbo;

	public Metrics() { }           // конструктор без аргументів

	public Metrics(int classCount,
	               int methodCount,
	               int cyclomaticComplexity,
	               int cbo) {      // робочий конструктор
		this.classCount = classCount;
		this.methodCount = methodCount;
		this.cyclomaticComplexity = cyclomaticComplexity;
		this.cbo = cbo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getClassCount() {
		return classCount;
	}

	public void setClassCount(int classCount) {
		this.classCount = classCount;
	}

	public int getMethodCount() {
		return methodCount;
	}

	public void setMethodCount(int methodCount) {
		this.methodCount = methodCount;
	}

	public int getCyclomaticComplexity() {
		return cyclomaticComplexity;
	}

	public void setCyclomaticComplexity(int cyclomaticComplexity) {
		this.cyclomaticComplexity = cyclomaticComplexity;
	}

	public int getCbo() {
		return cbo;
	}

	public void setCbo(int cbo) {
		this.cbo = cbo;
	}

}
