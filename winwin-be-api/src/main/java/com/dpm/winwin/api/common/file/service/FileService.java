package com.dpm.winwin.api.common.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile multipartFile, Long memberId, String directory);
    String getDefaultRandomProfileImageUrl();
    void deleteFile(String fileUrl);
}
