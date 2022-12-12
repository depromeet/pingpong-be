package com.dpm.winwin.api.common.utils;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
    String upload(MultipartFile multipartFile, String directory);
}
