package com.example.demo.service;

import com.example.demo.modals.ContactFormDTO;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class GoogleSheetsService {

    private Sheets sheetsService;
    private final String SPREADSHEET_ID = "143-dpsmXDRu9pYI3JOcWLAVFqpQIgxyBcz98Yv6Q67Q";

    @PostConstruct
    public void init() throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();
    }

//    private Sheets getSheetsService() throws IOException, GeneralSecurityException {
//        GoogleCredentials credentials = GoogleCredentials.fromStream(
//                        new FileInputStream("src/main/resources/credentials.json"))
//                .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));
//
//        return new Sheets.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JacksonFactory.getDefaultInstance(),
//                new HttpCredentialsAdapter(credentials))
//                .setApplicationName("My App")
//                .build();
//    }

    private Sheets getSheetsService() throws IOException, GeneralSecurityException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(
                        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("credentials.json")))
                .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));

        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("My App")
                .build();
    }

    public void addRow(ContactFormDTO contactFormDTO) throws IOException {
        ValueRange body = new ValueRange()
                .setValues(List.of(List.of(
                        UUID.randomUUID(),
                        contactFormDTO.getName(),
                        contactFormDTO.getEmail(),
                        contactFormDTO.getPhone(),
                        contactFormDTO.getPracticeArea(),
                        contactFormDTO.getSubject(),
                        contactFormDTO.getMessage()
                )));

        log.info("Entering query by user {}", contactFormDTO.getName());

        sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, "Sheet1", body)
                .setValueInputOption("RAW")
                .execute();
    }
}
