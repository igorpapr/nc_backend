package net.dreamfteam.quiznet.service.impl;

import net.dreamfteam.quiznet.data.dao.ImageDao;
import net.dreamfteam.quiznet.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    final private ImageDao imageDao;

    public ImageServiceImpl(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @Override
    public String saveImage(MultipartFile image) {
        return imageDao.saveImage(image);
    }

    @Override
    public byte[] loadImage(String imageId) {
        return imageDao.loadImage(imageId);
    }

    @Override
    public void deleteImage(String imageId) {
        imageDao.deleteImage(imageId);
    }
}
