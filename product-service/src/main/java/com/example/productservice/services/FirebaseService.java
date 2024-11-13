package com.example.productservice.services;

import com.example.productservice.exception.CustomException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FirebaseService {
    @Value("${firebase.image-base-url}")
    private String imageBaseUrl;

    public String upload(MultipartFile imageFile, String prefixImageFolder) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        String imageName = generateFileName(imageFile.getOriginalFilename(), prefixImageFolder);

        bucket.create(imageName, imageFile.getBytes(), imageFile.getContentType());
        return getImageUrl(imageName);
    }

    public String getImageUrl(String imageName) {
        return imageBaseUrl + imageName.replace("/", "%2F") + "?alt=media";
    }

    public String getImageUrlToken(String imageName) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        var imgBaseUrl = imageBaseUrl + imageName.replace("/", "%2F");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(imgBaseUrl))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonResponse = response.body();
        JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(jsonResponse);
        String imageToken = jsonNode.get("downloadTokens").asText();
        return imgBaseUrl + "?alt=media&token=" + imageToken;
    }

    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

    private String generateFileName(String originalFileName, String prefixImageFolder) {
        String extension = getExtension(originalFileName);

        if (isValidExtension(extension)) {
            String imageUrl = prefixImageFolder + UUID.randomUUID().toString() + "." + extension;

            while (isFileExists(imageUrl)) {
                imageUrl = prefixImageFolder + UUID.randomUUID().toString() + "." + extension;
            }
            return imageUrl;
        } else {
            throw new CustomException("Invalid file type: " + extension, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isValidExtension(String extension) {
        List<String> validExtensions = List.of("png", "jpg", "jpeg", "gif", "bmp", "webp", "tiff", "svg", "ico");
        return extension != null && (validExtensions.contains(extension.toLowerCase()));
    }

    public boolean isFileExists(String imageName) {
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.get(imageName);
        return (blob != null && blob.exists());
    }

    public void delete(String imageUrl) throws IOException {
        String imageName = extractAndConvert(imageUrl);

        Bucket bucket = StorageClient.getInstance().bucket();

        if (StringUtils.isEmpty(imageName)) {
            throw new IOException("invalid file Image Name");
        }

        Blob blob = bucket.get(imageName);

        if (blob == null || !blob.exists()) {
            throw new IOException("Image Name not found");
        }

        blob.delete();
    }

    private String extractAndConvert(String url) {
        int startIndex = url.indexOf("/o/") + 3;
        int endIndex = url.indexOf("?");
        String extractedPath = url.substring(startIndex, endIndex);

        return extractedPath.replace("%2F", "/");
    }

    public List<String> getAllImages(String prefixImageFolder) {
        List<Blob> imageBlobs = new ArrayList<>();
        Bucket bucket = StorageClient.getInstance().bucket();

        for (Blob blob : bucket.list(Storage.BlobListOption.prefix(prefixImageFolder)).iterateAll()) {
            imageBlobs.add(blob);
        }

        return switch (prefixImageFolder) {
            case "product-image/" -> getLimitedImageUrls(imageBlobs, bucket, 37);
            case "blog-image/" -> getLimitedImageUrls(imageBlobs, bucket, 5);
            default -> getImageUrls(imageBlobs, bucket);
        };
    }

    private List<String> getLimitedImageUrls(List<Blob> imageBlobs, Bucket bucket, int limit) {
        return imageBlobs.stream()
                .sorted(Comparator.comparing(Blob::getName))
                .limit(limit)
                .map(blob -> generateImageUrl(blob, bucket))
                .collect(Collectors.toList());
    }

    private List<String> getImageUrls(List<Blob> imageBlobs, Bucket bucket) {
        return imageBlobs.stream()
                .map(blob -> generateImageUrl(blob, bucket))
                .collect(Collectors.toList());
    }

    private String generateImageUrl(Blob blob, Bucket bucket) {
        return "https://storage.googleapis.com/" + bucket.getName() + "/" + blob.getName();
    }
}
