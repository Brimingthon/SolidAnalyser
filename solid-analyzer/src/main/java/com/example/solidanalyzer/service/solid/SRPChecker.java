package com.example.solidanalyzer.service.solid;

import com.example.solidanalyzer.entity.FileEntity;
import com.example.solidanalyzer.entity.Violation;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SRPChecker implements SolidChecker {

	@Override
	public List<Violation> check(CompilationUnit cu, FileEntity fileEntity) {
		List<Violation> violations = new ArrayList<>();

		for (ClassOrInterfaceDeclaration cls : cu.findAll(ClassOrInterfaceDeclaration.class)) {
			Map<String, Integer> methodGroups = new HashMap<>();

			// Групуємо методи за префіксом назви (наприклад: get, set, calculate...)
			for (MethodDeclaration method : cls.getMethods()) {
				String prefix = method.getNameAsString().split("(?=[A-Z])")[0];
				methodGroups.put(prefix, methodGroups.getOrDefault(prefix, 0) + 1);
			}

			if (methodGroups.size() > 3) {  // клас має занадто різнопланові методи
				Violation v = new Violation();
				v.setPrinciple("SRP");
				v.setDescription("Клас " + cls.getName() +
						" містить методи, які мають різні призначення: " + methodGroups.keySet());
				v.setLocation("Клас: " + cls.getName());
				v.setFile(fileEntity);
				violations.add(v);
			}
		}
		return violations;
	}
}
