package net.dreamfteam.quiznet.data.dao;

import org.springframework.web.multipart.MultipartFile;

public interface ImageDao {

    String saveImage(MultipartFile image);

    byte[] loadImage(String imageId);

    void deleteImage(String imageId);
}
