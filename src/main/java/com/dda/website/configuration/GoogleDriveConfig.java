package com.dda.website.configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Configuration
public class GoogleDriveConfig {
    private static final String APPLICATION_NAME = "DriveDrop Academy";
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Bean
    public Drive googleDrive() throws GeneralSecurityException, IOException {
        FileInputStream serviceAccountStream = new FileInputStream("./src/main/resources/credentials.json");
        GoogleCredential credential = GoogleCredential.fromStream(serviceAccountStream)
                .createScoped(Collections.singleton(DriveScopes.DRIVE_FILE));
        return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
