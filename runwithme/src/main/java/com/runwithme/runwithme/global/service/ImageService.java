package com.runwithme.runwithme.global.service;

import com.runwithme.runwithme.global.entity.Image;
import com.runwithme.runwithme.global.repository.ImageRepository;
import com.runwithme.runwithme.global.utils.CacheUtils;
import com.runwithme.runwithme.global.utils.MultipartFileUtils;
import com.runwithme.runwithme.global.utils.S3Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final S3Utils s3Utils;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Optional<Image> optionalImage = imageRepository.findById(1L);
        Image image;

        if (optionalImage.isEmpty()) {
            Image defaultImage = Image.builder()
                    .originalName("defaultImage")
                    .savedName("d95c0fed-1cee-4bd0-95a3-a3f3485e9045.jpg")
                    .build();
            image = imageRepository.save(defaultImage);
        } else {
            image = optionalImage.get();
        }

        CacheUtils.put("defaultImage", image);
    }

    public Long save(MultipartFile multipartFile) throws IOException {
        MultipartFileUtils multipartFileUtils = new MultipartFileUtils(multipartFile);

        uploadToS3(multipartFileUtils);

        Image image = Image.builder()
                .originalName(multipartFileUtils.getOriginalFileName())
                .savedName(multipartFileUtils.getUuidFileName())
                .build();

        return imageRepository.save(image).getSeq();
    }

    private void uploadToS3(MultipartFileUtils multipartFileUtils) throws IOException {
        File file = multipartFileUtils.convertToFile().orElseThrow(() -> new IllegalArgumentException("잘못된 파일입니다."));
        s3Utils.upload(file, multipartFileUtils.getFileType(), multipartFileUtils.getUuidFileName());
    }
}
