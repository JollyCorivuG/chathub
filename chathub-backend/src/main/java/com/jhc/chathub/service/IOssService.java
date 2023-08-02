package com.jhc.chathub.service;

import com.jhc.chathub.common.resp.Response;
import org.springframework.web.multipart.MultipartFile;

public interface IOssService {
    Response<String> uploadFile(MultipartFile file);
}
