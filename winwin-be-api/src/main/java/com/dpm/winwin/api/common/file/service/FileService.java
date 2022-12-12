package com.dpm.winwin.api.common.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String upload(MultipartFile multipartFile, String directory);
}
