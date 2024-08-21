package com.example.blog_app_apis.services.impl;

import com.example.blog_app_apis.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // Get the original file name
        String name = file.getOriginalFilename();

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("File must have a name");
        }

        // random name generate file
        String randomID = UUID.randomUUID().toString();
        String fileName1=randomID.concat(name.substring(name.lastIndexOf(".")));

        // Generate a new file path
        String filePath = path + File.separator + fileName1;

        // Create folder if not exists
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }

        // Copy the file to the destination
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // return name;
        return fileName1;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        // Construct the full file path
        String fullPath = path + File.separator + fileName;

        // Open the file as an InputStream
        InputStream is = new FileInputStream(fullPath);

        return is;
    }
}
