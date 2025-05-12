package com.example.solidanalyzer.service.solid;

import com.example.solidanalyzer.entity.FileEntity;
import com.example.solidanalyzer.entity.Violation;
import com.github.javaparser.ast.CompilationUnit;

import java.util.List;

public interface SolidChecker {
	List<Violation> check(CompilationUnit cu, FileEntity fileEntity);
}