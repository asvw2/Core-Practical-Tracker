package com.example.cpacsummary.controller;

import com.example.cpacsummary.model.StudentSummary;
import com.example.cpacsummary.service.ExcelParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class UploadController {

    @Autowired
    private ExcelParserService excelParserService;

    // Store parsed data in memory for demo purposes!
    private AtomicReference<List<StudentSummary>> studentStore = new AtomicReference<>();

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            List<StudentSummary> students = excelParserService.parse(file.getInputStream());
            studentStore.set(students);
            model.addAttribute("students", students);
            return "summary";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to parse Excel: " + e.getMessage());
            return "index";
        }
    }

    @GetMapping("/api/students")
    @ResponseBody
    public List<String> getStudentNames() {
        List<StudentSummary> students = studentStore.get();
        if (students == null) return List.of();
        return students.stream().map(StudentSummary::getSurname).toList();
    }

    @GetMapping("/api/summary/{name}")
    @ResponseBody
    public StudentSummary getStudentSummary(@PathVariable String name) {
        List<StudentSummary> students = studentStore.get();
        if (students == null) return null;
        return students.stream().filter(s -> s.getSurname().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
