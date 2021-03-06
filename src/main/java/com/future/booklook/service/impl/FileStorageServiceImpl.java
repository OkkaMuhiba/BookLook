package com.future.booklook.service.impl;

import com.future.booklook.configuration.FileStorageProperties;
import com.future.booklook.exception.FileStorageException;
import com.future.booklook.exception.MyFileNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class FileStorageServiceImpl {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Path[] paths = {
                    this.fileStorageLocation.resolve(Paths.get("products")),
                    this.fileStorageLocation.resolve(Paths.get("users")),
                    this.fileStorageLocation.resolve(Paths.get("markets")),
                    this.fileStorageLocation.resolve(Paths.get("books"))
            };

            for (Path path : paths) {
                Files.createDirectories(path);
            }

//            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file, String folder) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Date date = new Date();
            String timeInString = new SimpleDateFormat("yyyyMMddhhmmss").format(date.getTime());
            StringBuilder builder = new StringBuilder();
            builder.append(timeInString);
            builder.append("-");
            builder.append(generateRandomString());
            builder.append("."+ FilenameUtils.getExtension(file.getOriginalFilename()));
            fileName = builder.toString();

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetFile = Paths.get(folder);
            Path targetLocation = this.fileStorageLocation.resolve(targetFile);
            Path finalPath = targetLocation.resolve(fileName);
            Files.copy(file.getInputStream(), finalPath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    private String generateRandomString(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 32;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
