package com.example.productservice.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.productservice.dto.FileUploadedDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
@Log4j2
public class FileUploadService {
    private final Cloudinary cloudinary;

    public FileUploadedDTO uploadFile(MultipartFile file) throws Exception{
        try {
            File uploadedFile = convertMultiPartToFile(file);
//            ImageResizer.resize(uploadedFile.getAbsolutePath(),uploadedFile.getAbsolutePath());
            Map uploadResult = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
            boolean isDeleted = uploadedFile.delete();
            if (isDeleted){
                log.info("File successfully deleted");
            }else
                log.warn("File doesn't exist");
            return new FileUploadedDTO(uploadResult.get("url").toString(), uploadResult.get("public_id").toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public FileUploadedDTO uploadRawFile(File uploadedFile) throws Exception{
        try {
//            ImageResizer.resize(uploadedFile.getAbsolutePath(),uploadedFile.getAbsolutePath());
            Map uploadResult = cloudinary.uploader().upload(uploadedFile, ObjectUtils.asMap("folder", "/metadata", "resource_type", "raw"));
            boolean isDeleted = uploadedFile.delete();
            if (isDeleted){
                log.info("File successfully deleted");
            }else
                log.warn("File doesn't exist");
            return new FileUploadedDTO(uploadResult.get("url").toString(), uploadResult.get("public_id").toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile(String publicId, boolean isMetadataFile) throws IOException {
        if (isMetadataFile) {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("folder", "/metadata"));
        } else {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
