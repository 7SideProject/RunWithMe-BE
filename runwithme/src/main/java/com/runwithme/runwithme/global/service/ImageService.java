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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final S3Utils s3Utils;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        CacheUtils.put("defaultImage", imageRepository.findById(1L).orElseThrow(IllegalArgumentException::new));
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
