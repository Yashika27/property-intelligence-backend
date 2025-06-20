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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Base64;
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

//    private Sheets getSheetsService() throws IOException, GeneralSecurityException {
//
//        log.info("test logger");
////        InputStream inputStream1 = new FileInputStream("src/main/resources/credentials.json");
////        log.info("inputStream1 {}", inputStream1.read());
////
////        URL resource = getClass().getClassLoader().getResource("credentials.json");
////        log.info("File URL: {}", resource);
//
//        InputStream inputStream2 = getClass().getClassLoader().getResourceAsStream("credentials.json");
////        InputStream inputStream2 = new ClassPathResource("credentials.json").getInputStream();
//
//        log.info("inputStream2 {}", inputStream2.read());
//
//        GoogleCredentials credentials = GoogleCredentials.fromStream(
//                        Objects.requireNonNull(inputStream2))
//                .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));
//
//        return new Sheets.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JacksonFactory.getDefaultInstance(),
//                new HttpCredentialsAdapter(credentials))
//                .setApplicationName("My App")
//                .build();
//    }

//    private Sheets getSheetsService() throws IOException, GeneralSecurityException {
//        String testVar = System.getenv("TEST_VAR");
//        log.info("testVar: {}", testVar);
//
//        String base64Credentials = System.getenv("GOOGLE_CREDENTIALS_BASE64");
//        if (base64Credentials == null) {
//            throw new IllegalStateException("Missing GOOGLE_CREDENTIALS_BASE64 env variable");
//        }
//
//        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
//        InputStream credentialsStream = new ByteArrayInputStream(decodedBytes);
//
//        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
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

    log.info("testVar: {}", testVar);

    String base64Encoded = "ewogICJ0eXBlIjogInNlcnZpY2VfYWNjb3VudCIsCiAgInByb2plY3RfaWQiOiAicHJvcGVydHktaW50ZWxsaWdlbmNlLTQ2MDExNCIsCiAgInByaXZhdGVfa2V5X2lkIjogImM1N2FiMmEyNTFhNzJiMzIyZDYzYzgxYjUxYjM2ZjBiZjc1ZWI1N2EiLAogICJwcml2YXRlX2tleSI6ICItLS0tLUJFR0lOIFBSSVZBVEUgS0VZLS0tLS1cbk1JSUV2UUlCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktjd2dnU2pBZ0VBQW9JQkFRQ2FYVThKS21yN1dSeUVcbnhVcEJFaWYzenBiSlp2d3hRVWJ0VDF0OFcydDVnbkkwbjBOVjBCa211UEE5TGVXSHBzeHFFZGxDMElaNXRFMUhcblljS0w5dnkyUWdFbnFtbWxhVnBiRGN0TmVrOFd1UkhyWGtLTDBybEhCVldQTER3OEVST0t2SGZOQXdVY2lvdjdcbkdRQzg2aFBJbkFpcjRid0o3aVhjMFo3a1A4SkJZeE1WMDYzRk42QjJmaDN3RGd6YnltVThMcmg2MjN3eTNGWTJcbk1RLzZMaDZpZnNRajNYZ1NDanFnWW1yWE9ZTWR0V0tMM1lsWWUzWUpTQkdscENlWjJMVXFzak13YkNucWRyTEFcbkRweVhwZXVLQ2JlbnhzTER5TEhkYlF6SnJLWnNlVUR6Q3l1S2VEMVRyRW1oN3hLQUtxaVczc1dmcWpSaUxlaURcbkgvTzF6RjNGQWdNQkFBRUNnZ0VBRWs0SlRzbHdhVURlY0dJaTFZZU5KTVlqUEZ2UmdSZkFET2tSckxQOGtTcW1cblYxWWhlbjRVVlhLdHpsdHdjZjhXRTJmdGJ3NWlPL0ZsRHBkcHBnWlNYbHlDWFkxSVBOeTY0YnhIaFBjQmhPektcblVySlpTdEpndFNyTlJBNlhUL29vYXBYQjBSWlFGMXZZa0p5YzZVb0tzWW5MUDNLanhUdXYwcmtxMGQ4N3Jsd2Rcbm4zZDJ0ZWxyMm9UWUZhUDg0Ky9YbDJ1a2ZvOHdMTnp3V0ZtdjhSb2E4NzhmNXEzNW9VVUtabjR0UjZYK01SRklcblV1UFdjNDJrU3AySDcwc0R1K2t5YVlEb2JaUXp5YlorSDRKbnNBTGZGYXdMaGtKWlREZDYyOThHem94bGtIY3FcbnMxUWR4cm9tM1RGT05EZktlRFhJZ3ZZMVk5S0NSQVVYd1R2MGtNcG42d0tCZ1FETWZGdkVDeDZ4ZjB3enFFN0ZcbjQ4RnZtd3E2K2hQUndjcFBBSC9JM3NEcSsrYzlHdzViYUFicG1URkpjZ29xakU4ZHdvL2RXbU1JVXRNWVJYQkRcbjVRcVBScEFzenU1SVo4bkMySGswbkU3STFvVTVtYlVySEwzMDVWclFsa0lQYjFuRkZCalVNdTFtNWluZEI1M0dcbkF3QzFjVXByQ3lWZjZwdTF1QjJ4OTA2Qmp3S0JnUURCUUlxcXY0TUcrMEcwOTBsbHRJZ1BtRDBrdG1iVUQ0L0tcbklMbDhwSnQzSnNkZWhnbXY1dGxnandFdEQrUmNnSjN4UGhzK25iRGVHVGd5VWVRODBGUmpUWVJGK1Q3OEZIN2JcbnRqaUYzS2RzLzNtVkhybDVReXdyc0M1NTRWSXRTVG1xbVFDekw2K3QwUm5DRFBmc2pIQ3Fvc2ZQOXNTekREQkJcbmtwdHNDRGJaYXdLQmdHWFBqUWtBMkZsYm5oOGlMNFpMcmFYWnRlLytUeHZpdHMyNjIrU3hNN0JMbDRoUERram9cbmRIaWVNM0h6KzZmUGtCeXJJR0gvZk10S0ladzJCbjFuYm1pRG80a0x4b3Y2VVBSWVA1QzRPbW00bFVCbWpWTlhcbnppUStoWWJSS1RieUkrb0I1WkxSZ0czNUx1TER4Z1F1dm8yWVNJc2ZVNDU4Q3ViaE1yZkhBQ2wxQW9HQU1tV2xcbmcvL3JxSzl2cEVUMUJDdi9hNm0xRUZUdEhoSnMyamFNaU4vWWwwc3FwUkRwaDlENUVZQlp1eW13MFJyTmxiTXZcbm9iVzRBVU9zYnhzQ0hZWHE2L2EwTHhkZ0FKZEx1ZnFjWlBSMVNTeWF4WE84eE0zcXYvM3pNTis4OFlodUhIcElcblAzS0pLeGdqMG9KVlNZQWFoYlZyekdVN1NNSTk5MnZIVW5ZVjc3OENnWUVBdDdzczB1ZWNQZFJDb0cwRE81N2NcbnNQbjJrR3RGMkc0Z1JZeWViOTMxVlRNaktTSGpDT0FBWURrLzlMRjUwRi9CUUhncEdOQ1ZVWDBSY2RxM1dWVnNcbmg4aUN3em5sSm9rK0h4TnVjcjRDV1EzdjV0cG1LZzNzL0NLWXB5YkFtRWc1UDR2ODYra045c3grTkQ5WHMxMkZcbjJnTll2LzdDRGJRcXU1ZlJNUDhmZWxzPVxuLS0tLS1FTkQgUFJJVkFURSBLRVktLS0tLVxuIiwKICAiY2xpZW50X2VtYWlsIjogInlhc2hpa2FsYW1iYUBwcm9wZXJ0eS1pbnRlbGxpZ2VuY2UtNDYwMTE0LmlhbS5nc2VydmljZWFjY291bnQuY29tIiwKICAiY2xpZW50X2lkIjogIjExNzE5MTU1MDI1NjQzNzcwMTU1MiIsCiAgImF1dGhfdXJpIjogImh0dHBzOi8vYWNjb3VudHMuZ29vZ2xlLmNvbS9vL29hdXRoMi9hdXRoIiwKICAidG9rZW5fdXJpIjogImh0dHBzOi8vb2F1dGgyLmdvb2dsZWFwaXMuY29tL3Rva2VuIiwKICAiYXV0aF9wcm92aWRlcl94NTA5X2NlcnRfdXJsIjogImh0dHBzOi8vd3d3Lmdvb2dsZWFwaXMuY29tL29hdXRoMi92MS9jZXJ0cyIsCiAgImNsaWVudF94NTA5X2NlcnRfdXJsIjogImh0dHBzOi8vd3d3Lmdvb2dsZWFwaXMuY29tL3JvYm90L3YxL21ldGFkYXRhL3g1MDkveWFzaGlrYWxhbWJhJTQwcHJvcGVydHktaW50ZWxsaWdlbmNlLTQ2MDExNC5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsCiAgInVuaXZlcnNlX2RvbWFpbiI6ICJnb29nbGVhcGlzLmNvbSIKfQo=";

    GoogleCredentials credentials = GoogleCredentials
            .fromStream(new ByteArrayInputStream(Base64.getDecoder().decode(base64Encoded)))
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
//                        UUID.randomUUID(),
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
