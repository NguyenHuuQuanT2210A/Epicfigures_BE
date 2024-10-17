package com.example.productservice.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {
    @Value("${firebase.bucket-name}")
    private String firebaseBucketName;

    @Bean
    public FirebaseApp initFirebase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            //        String serviceAccountPath = System.getProperty("user.dir") + "/epicgure-firebase-key.json";
//        FileInputStream serviceAccountStream = new FileInputStream(serviceAccountPath);

//        ClassPathResource serviceAccount = new ClassPathResource("epicgure-firebase-key.json");

            String serviceAccountJson = "{"
                    + "\"type\": \"service_account\","
                    + "\"project_id\": \"epicgure\","
                    + "\"private_key_id\": \"2ed3e199471aaefe809fdc5d53f04ac1eb2884c6\","
                    + "\"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDVH+BcoYGGnz8b\\n5kcpKyYzlkm2ss/DfhPggkUwmAw5kDnqrbGwRmEVnAYzQwQAP+Z0WxV8b5TrvZ6x\\nhIEC8SFpRa/+qzllVPO5aap2KRReEEsgB/aZpYGAlzBphabBOPoJ//p9KnjT/hnq\\n3YWsHuLrmlLOuX2J4G5LmJmurhcoaMbEW0zwtuiNj+CtgepEJCjH4WnpcddqXx1i\\nF1Gg/NIrE2FXVjzNHRx2aTU1BmjryQWlo2sWp+XfG+05zO16dnS6nMKwIHZQWuzs\\n37tCHzh5ghTFmay/w9zCvG5wCc2oWetYSlAYIQjtjDR+24XUJVY7jmTIaEQREFTQ\\nITTt6dNxAgMBAAECggEAAWAsNxovquB7xTe/uOWAUnxsRjf8wkbPlqVfuwO1IEVw\\narW9QXdHuKWuiVdy7SJ8dRQ0NfCNcZ2NByeW1F+56oMRq8ghGj4JuF8VCrx9iNw8\\nAiDkzugkDlPn8xCkl9P5y6VzfBvYi99RGeZAp8YECudV28ciA5BK/fb7gWosNLSj\\n2+vRh6ixKr6PyHb5JaU3n3UWiuJGHqcyH8KIkPZbwtS79xLC+BHU82Jr8w560S+S\\nOezIuttgi3oJL+JsOepBg49jlTPp44fB5dG/FDtwPwqzB8C/XivOnvG4FP0p02X+\\nM6DMjrWaYBhzQCGkr34f9Wq0AF91m5VlKYxVhbL4TQKBgQDy8wMyuFmE5DlzAiXE\\nfHftrCMx86o/MChIboXgFWE/EI0GaPBOinkfjYLFUtY0NmoxSP5aE27nc8mT66dz\\nLO+0yaXXdQK1osJ0Mi2Fkl6+hYcVL9v2/GvFLnJ/vwc8sL2nBXePo1CPCBM8vep/\\nAGHo5ON6BbcVYBpJ9fLswJL5LQKBgQDgkrhelVupj/v0/bESSughfCwWy0b+5ZYg\\n14jXVSe4GwPhpUnh/YD11hmFu6KkF6i2YAi9hA6kPOCL86AKOkXbCF9XISFLaWBE\\nzpzjjIHn2ijYnVKF/Nzu5e5efJ2vCV7iIUzqaLcCEm1gxFnY3s3/FwcefUpVYPXc\\nJ5fbVDEl1QKBgECokHdVPO3zV57WOXr7rWdh4tQOh4bdqDruv06OIh2IqB/Gmf4t\\nOGM/ZEhj7YJj4QtQ4/DRLzkGReXKV/x9PyacbDjVeW0h7iLCn/7mn7SREQyAekhX\\nkc8et3ZsPt7WIwNqnxFpY61NduE5okk+rtAu6qO1Fa7ortZ0ULzV28fZAoGBAI1W\\n6mDlAda7PylXDb3klSqV+y2xbJJ6rr0mygI0nxX5Mr0uiUyJf0O6J0AK8vbxyQZX\\ndMjKN2jYw75xa7VkLZkIMPEmcc0DmkpT+1wfj5b7sB99DHY1yls1LYwcRCXo8OXD\\nbaROmXfdXye7DI041U+KD7n5wvHvX23bIXEyEOitAoGBAIcVxxNA8kWq3gdhfDJS\\nQsn/fHkEKoxoBAfXS01vvK/bwjAi4ndv/1w7zXpO8B23g2Jh3v4LE8VUWfYdE2bO\\nH6pT0rP666ghIerCp2kgnpTtvuV+OczrskYp5ngAbIV0hxIsSLmJaKkw5AeKfbDO\\nSjoyLJPrJzFisVRt1q2OEO5C\\n-----END PRIVATE KEY-----\\n\","
                    + "\"client_email\": \"firebase-adminsdk-90frf@epicgure.iam.gserviceaccount.com\","
                    + "\"client_id\": \"106097388418247654166\","
                    + "\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\","
                    + "\"token_uri\": \"https://oauth2.googleapis.com/token\","
                    + "\"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\","
                    + "\"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-90frf%40epicgure.iam.gserviceaccount.com\","
                    + "\"universe_domain\": \"googleapis.com\""
                    + "}";

            ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(serviceAccountJson.getBytes(StandardCharsets.UTF_8));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .setStorageBucket(firebaseBucketName)
                    .build();

            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }
}
