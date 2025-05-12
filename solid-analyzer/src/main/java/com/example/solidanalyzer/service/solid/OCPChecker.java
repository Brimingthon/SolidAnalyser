package com.example.solidanalyzer.service.solid;

import com.example.solidanalyzer.entity.FileEntity;
import com.example.solidanalyzer.entity.Violation;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.ArrayList;
import java.util.List;

public class OCPChecker implements SolidChecker {

	@Override
	public List<Violation> check(CompilationUnit cu, FileEntity ctx) {
		List<Violation> list = new ArrayList<>();

		// instanceof
		cu.findAll(InstanceOfExpr.class).forEach(expr -> {
			int line = expr.getBegin().map(p -> p.line).orElse(-1);

			Violation v = new Violation();
			v.setPrinciple("OCP");
			v.setDescription("Перевірка instanceof «" + expr.getType()
					+ "» обмежує розширюваність.");
			v.setLocation("Рядок " + line);
			v.setFile(ctx);
			list.add(v);
		});

		// getClass()
		cu.findAll(MethodCallExpr.class).stream()
				.filter(mc -> mc.getNameAsString().equals("getClass"))
				.forEach(mc -> {
					int line = mc.getBegin().map(p -> p.line).orElse(-1);

					Violation v = new Violation();
					v.setPrinciple("OCP");
					v.setDescription("Виклик getClass() для вибору логіки порушує OCP.");
					v.setLocation("Рядок " + line);
					v.setFile(ctx);
					list.add(v);
				});

		return list;
	}
}

