package com.runwithme.runwithme.global.service;

import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.error.CustomException;
import com.runwithme.runwithme.global.repository.ImageRepository;
import com.runwithme.runwithme.global.utils.ImageCache;
import com.runwithme.runwithme.global.utils.MultipartFileUtils;
import com.runwithme.runwithme.global.utils.S3Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

import static com.runwithme.runwithme.global.result.ResultCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final String DEFAULT_PROFILE_SAVED_NAME = "default.jpg";
    private final String DEFAULT_CHALLENGE_SAVED_NAME = "defaultChallenge.jpg";


    private final ImageRepository imageRepository;

    private final S3Utils s3Utils;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        ImageCache.put(ImageCache.DEFAULT_PROFILE, getDefaultImage(DEFAULT_PROFILE_SAVED_NAME));
        ImageCache.put(ImageCache.DEFAULT_CHALLENGE, getDefaultImage(DEFAULT_CHALLENGE_SAVED_NAME));
    }

    private Image getDefaultImage(String name) {
        Optional<Image> optionalImage = imageRepository.findBySavedName(name);
        return optionalImage.orElseGet(() -> saveDefaultImage(name));
    }

    private Image saveDefaultImage(String name) {
        Image image;
        Image defaultImage = Image.builder()
                .originalName(name)
                .savedName(name)
                .build();
        image = imageRepository.save(defaultImage);
        return image;
    }

    public Image save(MultipartFile multipartFile) {
        MultipartFileUtils multipartFileUtils = new MultipartFileUtils(multipartFile);

        uploadToS3(multipartFileUtils);

        Image image = Image.builder()
                .originalName(multipartFileUtils.getOriginalFileName())
                .savedName(multipartFileUtils.getUuidFileName())
                .build();

        return imageRepository.save(image);
    }

    public void delete(Long imageSeq) {
        Image image = imageRepository.findById(imageSeq).orElseThrow(() -> new CustomException(IMAGE_NOT_FOUND));

        image.delete();

        s3Utils.delete("image", image.getSavedName());
    }

    public Resource getImage(Long imageSeq) {
        Image image = imageRepository.findById(imageSeq).orElseThrow(() -> new CustomException(IMAGE_NOT_FOUND));
        return s3Utils.download("image", image.getSavedName());
    }

    private void uploadToS3(MultipartFileUtils multipartFileUtils) {
        File file = multipartFileUtils.convertToFile().orElseThrow(() -> new CustomException(FAILED_CONVERT));
        s3Utils.upload(file, multipartFileUtils.getFileType(), multipartFileUtils.getUuidFileName());
    }
}
