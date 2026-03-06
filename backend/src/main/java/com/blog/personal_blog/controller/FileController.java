package com.blog.personal_blog.controller;

import com.blog.personal_blog.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @PostMapping("/upload")
    public R<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.error("文件不能为空");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String newFilename = UUID.randomUUID().toString() + extension;

            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path uploadDir = Paths.get(uploadPath, datePath);

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);

            String fileUrl = "/uploads/" + datePath + "/" + newFilename;

            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("filename", originalFilename);

            return R.success(result);
        } catch (IOException e) {
            return R.error("文件上传失败：" + e.getMessage());
        }
    }
}
