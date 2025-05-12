package com.example.solidanalyzer.service;

import com.example.solidanalyzer.entity.FileEntity;
import com.example.solidanalyzer.entity.Metrics;
import com.example.solidanalyzer.entity.Violation;
import com.example.solidanalyzer.repository.FileRepository;
import com.example.solidanalyzer.service.solid.*;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.WhileStmt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AnalysisService {

	private final FileRepository fileRepository;
	private final List<SolidChecker> checkers;

	@Autowired
	public AnalysisService(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
		// реєструємо усі перевірники, які наразі підтримуємо
		this.checkers = Arrays.asList(
				new SRPChecker(),
				new DIPChecker(),
				new OCPChecker(),
				new ISPChecker()
		);
	}

	/**
	 * Аналізує файл, зберігає метрики та порушення у БД і повертає
	 * збережений об’єкт FileEntity.
	 */
	public FileEntity analyzeAndSave(MultipartFile file) throws IOException {

		/* ---------- 1. Налаштовуємо JavaParser на Java 17 ---------- */
		ParserConfiguration cfg = new ParserConfiguration()
				.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);

		JavaParser parser = new JavaParser(cfg);
		ParseResult<CompilationUnit> parse = parser.parse(file.getInputStream());

		if (!parse.isSuccessful() || parse.getResult().isEmpty()) {
			throw new IOException("Не вдалося розібрати файл: "
					+ parse.getProblems());
		}
		CompilationUnit cu = parse.getResult().get();

		/* ---------- 2. Рахуємо елементарні метрики ---------- */
		int classCount   = cu.findAll(ClassOrInterfaceDeclaration.class).size();
		int methodCount  = cu.findAll(MethodDeclaration.class).size();
		int complexity   = cu.findAll(IfStmt.class).size()
				+ cu.findAll(ForStmt.class).size()
				+ cu.findAll(WhileStmt.class).size()
				+ cu.findAll(SwitchEntry.class).size();
		int cbo          = cu.findAll(FieldDeclaration.class).size();

		Metrics metrics = new Metrics(classCount, methodCount, complexity, cbo);

		/* ---------- 3. Готуємо об’єкт FileEntity ---------- */
		FileEntity fileEntity = new FileEntity();
		fileEntity.setFileName(file.getOriginalFilename());
		fileEntity.setAnalysisDate(LocalDateTime.now());
		fileEntity.setMetrics(metrics);
		fileEntity.setViolations(new ArrayList<>());

		/* ---------- 4. Запускаємо всі перевірки SOLID ---------- */
		for (SolidChecker checker : checkers) {
			List<Violation> found = checker.check(cu, fileEntity);
			fileEntity.getViolations().addAll(found);
		}

		/* ---------- 5. Зберігаємо результат у БД ---------- */
		return fileRepository.save(fileEntity);
	}
}
