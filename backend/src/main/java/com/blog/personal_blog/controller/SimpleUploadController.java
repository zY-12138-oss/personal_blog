package com.blog.personal_blog.controller;

import com.blog.personal_blog.common.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/simple")
public class SimpleUploadController {

    @PostMapping("/upload")
    public R<Map<String, String>> simpleUpload(@RequestParam("file") MultipartFile file) {
        System.out.println("=== SimpleUploadController 被调用了！===");
        System.out.println("文件名: " + file.getOriginalFilename());
        System.out.println("文件大小: " + file.getSize());
        
        if (file.isEmpty()) {
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
                uploadDir.mkdirs();
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
        } catch (Exception e) {
            System.out.println("=== 文件上传失败 ===");
            e.printStackTrace();
            return R.error("文件上传失败：" + e.getMessage());
        }
    }

    @GetMapping("/test")
    public R<String> test() {
        System.out.println("=== SimpleUploadController test 被调用了！===");
        return R.success("SimpleUploadController正常！");
    }
}
