package com.example.demo.controller;

import com.example.demo.modals.ContactFormDTO;
import com.example.demo.service.GoogleSheetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "https://property-intelligence-solutions.vercel.app")
@RestController
@RequestMapping("/api/contact")
public class FormController {

    @Autowired
    private GoogleSheetsService sheetsService;

    @PostMapping("/submit")
    public ResponseEntity<String> handleSubmit(
            @RequestBody ContactFormDTO contactFormDTO) {
        try {
            sheetsService.addRow(contactFormDTO);
            return ResponseEntity.ok("Form submitted successfully!");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}
