-- 为现有数据库添加角色字段的迁移脚本
USE personal_blog;

-- 添加角色字段（如果不存在）
ALTER TABLE users ADD COLUMN IF NOT EXISTS role ENUM('USER', 'ADMIN') DEFAULT 'USER' AFTER bio;

-- 插入默认管理员账号 (密码: admin123，使用BCrypt加密
-- 注意：如果已存在admin用户，请先删除或修改
INSERT INTO users (username, email, password, role) VALUES 
('admin', 'admin@blog.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ADMIN')
ON DUPLICATE KEY UPDATE role='ADMIN', password='$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG';
