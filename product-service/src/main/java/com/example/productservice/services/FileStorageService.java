package com.example.productservice.services;


import com.example.productservice.configs.FileStorageProperties;
import com.example.productservice.exception.CustomException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileLogoStorageLocation;
    private final Path fileProductImageStorageLocation;
    private final Path fileUserImageStorageLocation;
    private final Path fileThumbnailImageStorageLocation;

    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileLogoStorageLocation = Paths.get(fileStorageProperties.getUploadLogoDir())
                .toAbsolutePath().normalize();
        this.fileProductImageStorageLocation = Paths.get(fileStorageProperties.getUploadProductImageDir())
                .toAbsolutePath().normalize();
        this.fileUserImageStorageLocation = Paths.get(fileStorageProperties.getUploadUserImageDir())
                .toAbsolutePath().normalize();
        this.fileThumbnailImageStorageLocation = Paths.get(fileStorageProperties.getUploadThumbnailImageDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileLogoStorageLocation);
            Files.createDirectories(fileProductImageStorageLocation);
            Files.createDirectories(fileUserImageStorageLocation);
            Files.createDirectories(fileThumbnailImageStorageLocation);
        }catch (Exception ex){
            throw new CustomException("File upload fail " + ex, HttpStatus.BAD_REQUEST);
        }
    }

    public  String storeLogoFile(MultipartFile file){
        return storeFile(fileLogoStorageLocation,file);
    }
    public  String storeProductImageFile(MultipartFile file){
        return storeFile(fileProductImageStorageLocation,file);
    }

    public  String storeUserImageFile(MultipartFile file){
        return storeFile(fileUserImageStorageLocation,file);
    }

    public String storeThumbnailImageFile(MultipartFile file){
        return storeFile(fileThumbnailImageStorageLocation,file);
    }

    private String storeFile(Path location, MultipartFile file){
        UUID uuid = UUID.randomUUID();

        String ext  = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = uuid + "." + ext;
        try{
            if(filename.contains("..")){
                throw  new CustomException("Sorry! Filename contains invalid", HttpStatus.BAD_REQUEST);
            }
            Path targetLocation = location.resolve(filename);
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        }catch (Exception ex){
            throw new CustomException("Could not store file " + ex, HttpStatus.BAD_REQUEST);
        }
    }

    public Resource loadLogoFileAsResource(String filename){
        return  loadFileAsResource(fileLogoStorageLocation,filename);
    }

    public Resource loadProductImageFileAsResource(String filename){
        return  loadFileAsResource(fileProductImageStorageLocation,filename);
    }

    public Resource loadUserImageFileAsResource(String filename){
        return  loadFileAsResource(fileUserImageStorageLocation,filename);
    }

    public Resource loadThumblnailImageFileAsResource(String filename){
        return  loadFileAsResource(fileThumbnailImageStorageLocation,filename);
    }

    private Resource loadFileAsResource(Path location,String filename){
        try {
            Path filePath = location.resolve(filename).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            }else {
                throw new CustomException("File not found"+ filename, HttpStatus.NOT_FOUND);
            }
        }catch (Exception ex){
            throw new CustomException("File not found"+ filename + ex, HttpStatus.NOT_FOUND);
        }
    }

    public void deleteLogoFile(String filename){
        deleteFile(fileLogoStorageLocation,filename);
    }

    public void deleteProductImageFile(String filename){
        deleteFile(fileProductImageStorageLocation,filename);
    }

    public void deleteUserImageFile(String filename){
        deleteFile(fileUserImageStorageLocation,filename);
    }

    public void deleteThumbnailImageFile(String filename){
        deleteFile(fileThumbnailImageStorageLocation,filename);
    }

    private void deleteFile(Path location, String filename){
        try {
            Path filePath = location.resolve(filename).normalize();


            if (!Files.exists(filePath)) {
                throw new CustomException("File not found "+ filename, HttpStatus.NOT_FOUND);
            }
            Files.delete(filePath);
        }catch (Exception ex){
            throw new CustomException("File not found "+ filename + ex, HttpStatus.NOT_FOUND);
        }
    }
}
