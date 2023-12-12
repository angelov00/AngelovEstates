package com.softuni.angelovestates.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FileUploadService {

    private final Cloudinary cloudinary;

    @Autowired
    public FileUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public List<String> uploadOfferPhotos(List<MultipartFile> file) throws IOException {

        try {
            List<String> urls = new ArrayList<>();
            for (MultipartFile multipartFile : file) {

                Map<String, String> test = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
                urls.add(test.get("url"));
            }
            return urls;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("CLOUDINARY ERROR");
        }

    }

    public String uploadAgentPhoto(MultipartFile file) throws IOException {

        try {
            Map<String, String> test = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return test.get("url");

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("CLOUDINARY ERROR");
        }

    }

}
