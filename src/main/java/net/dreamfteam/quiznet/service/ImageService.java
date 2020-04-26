package net.dreamfteam.quiznet.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String saveImage(MultipartFile image);

    byte[] loadImage(String imageId);

    void deleteImage(String imageId);

}
