package com.example.solidanalyzer.controller;

import com.example.solidanalyzer.entity.FileEntity;
import com.example.solidanalyzer.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
public class AnalysisController {

	private final AnalysisService analysisService;

	@Autowired               // інжекція через конструктор
	public AnalysisController(AnalysisService analysisService) {
		this.analysisService = analysisService;
	}

	@GetMapping("/")
	public String uploadPage() {
		return "upload";
	}

	@PostMapping("/analyze")
	public String analyzeFile(@RequestParam("file") MultipartFile file,
	                          Model model) {
		try {
			FileEntity result = analysisService.analyzeAndSave(file);
			model.addAttribute("result", result);
		} catch (IOException e) {
			model.addAttribute("error",
					"Помилка аналізу файлу: " + e.getMessage());
		}
		return "report";
	}
}

