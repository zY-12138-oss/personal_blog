package com.blog.personal_blog.controller;

import com.blog.personal_blog.common.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @PostMapping("/upload")
    public R<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println("=== 开始处理文件上传 ===");
        System.out.println("文件名: " + file.getOriginalFilename());
        System.out.println("文件大小: " + file.getSize());

        if (file.isEmpty()) {
            System.out.println("文件为空");
            return R.error("文件不能为空");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + extension;

            String uploadDirPath = System.getProperty("user.dir") + File.separator + "uploads";
            File uploadDir = new File(uploadDirPath);
            if (!uploadDir.exists()) {
                System.out.println("创建目录: " + uploadDirPath);
                boolean created = uploadDir.mkdirs();
                System.out.println("目录创建结果: " + created);
            }

            String filePath = uploadDirPath + File.separator + newFilename;
            System.out.println("保存文件到: " + filePath);
            file.transferTo(new File(filePath));

            String fileUrl = "/uploads/" + newFilename;
            System.out.println("文件URL: " + fileUrl);

            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("filename", originalFilename);

            System.out.println("=== 文件上传成功 ===");
            return R.success(result);
        } catch (IOException e) {
            System.out.println("=== 文件上传失败 ===");
            e.printStackTrace();
            return R.error("文件上传失败：" + e.getMessage());
        }
    }
}
