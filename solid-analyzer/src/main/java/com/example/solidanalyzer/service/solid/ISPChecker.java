package com.example.solidanalyzer.service.solid;

import com.example.solidanalyzer.entity.FileEntity;
import com.example.solidanalyzer.entity.Violation;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.ArrayList;
import java.util.List;

public class ISPChecker implements SolidChecker {

	private static final int MAX_METHODS = 6;

	@Override
	public List<Violation> check(CompilationUnit cu, FileEntity ctx) {
		List<Violation> out = new ArrayList<>();

		cu.findAll(ClassOrInterfaceDeclaration.class).forEach(ci -> {
			if (ci.isInterface() && ci.getMethods().size() > MAX_METHODS) {
				Violation v = new Violation();
				v.setPrinciple("ISP");
				v.setDescription("Інтерфейс " + ci.getName()
						+ " має " + ci.getMethods().size()
						+ " методів. Рекомендовано розбити за ролями.");
				v.setLocation("Інтерфейс: " + ci.getName());
				v.setFile(ctx);
				out.add(v);
			}

			if (!ci.isInterface()) {
				long empty = ci.getMethods().stream()
						.filter(m -> m.getBody().isPresent()
								&& m.getBody().get().isEmpty())
						.count();
				if (empty > 0 && !ci.getImplementedTypes().isEmpty()) {
					Violation v = new Violation();
					v.setPrinciple("ISP");
					v.setDescription("Клас " + ci.getName()
							+ " реалізує інтерфейс, але не використовує "
							+ empty + " метод(ів). Можливе порушення ISP.");
					v.setLocation("Клас: " + ci.getName());
					v.setFile(ctx);
					out.add(v);
				}
			}
		});
		return out;
	}
}
