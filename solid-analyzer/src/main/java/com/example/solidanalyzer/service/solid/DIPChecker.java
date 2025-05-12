package com.example.solidanalyzer.service.solid;

import com.example.solidanalyzer.entity.FileEntity;
import com.example.solidanalyzer.entity.Violation;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.util.ArrayList;
import java.util.List;

public class DIPChecker implements SolidChecker {

	@Override
	public List<Violation> check(CompilationUnit cu, FileEntity fileEntity) {
		List<Violation> violations = new ArrayList<>();

		for (FieldDeclaration field : cu.findAll(FieldDeclaration.class)) {
			for (VariableDeclarator var : field.getVariables()) {
				String typeName = var.getType().asString();
				if (!typeName.startsWith("I") && !typeName.contains("List")
						&& Character.isUpperCase(typeName.charAt(0))) {

					Violation v = new Violation();
					v.setPrinciple("DIP");
					v.setDescription("Клас залежить від конкретного типу '" + typeName +
							"' замість інтерфейсу або абстракції.");
					v.setFile(fileEntity);
					v.setLocation("Клас: " + var.getName());
					violations.add(v);
				}
			}
		}
		return violations;
	}
}
