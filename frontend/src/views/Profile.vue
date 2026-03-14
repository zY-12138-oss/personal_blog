<template>
  <div class="profile">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1 @click="$router.push('/articles')" style="cursor: pointer">个人博客</h1>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </el-header>
      <el-main>
        <el-card>
          <template #header>
            <div class="card-header">
              <span>个人中心</span>
            </div>
          </template>
          <el-form :model="userForm" class="profile-form" label-position="top">
            <el-form-item label="用户名" class="form-item">
              <el-input v-model="userForm.username" disabled />
            </el-form-item>
            <el-form-item label="邮箱" class="form-item">
              <el-input v-model="userForm.email" disabled />
            </el-form-item>
            <el-form-item label="简介" class="form-item">
              <el-input v-model="userForm.bio" type="textarea" :rows="4" />
            </el-form-item>
            <el-form-item label="头像" class="form-item">
              <div class="avatar-section">
                <el-avatar :size="100" :src="userForm.avatar" class="user-avatar">
                  {{ userForm.username ? userForm.username.charAt(0).toUpperCase() : 'U' }}
                </el-avatar>
                <div class="avatar-upload">
                  <input
                    type="file"
                    ref="avatarInput"
                    @change="handleAvatarSelect"
                    accept="image/*"
                    style="display: none"
                  />
                  <el-button type="primary" :icon="Picture" @click="triggerAvatarUpload" class="upload-btn">上传头像</el-button>
                  <el-input v-model="userForm.avatar" placeholder="或输入头像 URL" class="avatar-input" />
                </div>
              </div>
            </el-form-item>
            <el-form-item class="form-item">
              <el-button type="primary" @click="handleUpdate" class="save-btn">保存修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Picture } from '@element-plus/icons-vue'
import { jwtDecode } from 'jwt-decode'
import { useUserStore } from '@/stores/user'
import { getUserInfo, updateUserInfo } from '@/api/user'
import { uploadFile } from '@/api/file'

const router = useRouter()
const userStore = useUserStore()

const userForm = reactive({
  username: '',
  email: '',
  bio: '',
  avatar: ''
})

const userId = ref(null)
const avatarInput = ref(null)

const loadUserInfo = async () => {
  if (userStore.token) {
    try {
      const decoded = jwtDecode(userStore.token)
      userId.value = parseInt(decoded.sub)
      const res = await getUserInfo(userId.value)
      userForm.username = res.data.username
      userForm.email = res.data.email
      userForm.bio = res.data.bio || ''
      userForm.avatar = res.data.avatar || ''
    } catch (error) {
      console.error('加载用户信息失败', error)
    }
  }
}

const handleUpdate = async () => {
  try {
    await updateUserInfo(userId.value, {
      bio: userForm.bio,
      avatar: userForm.avatar
    })
    ElMessage.success('保存成功')
  } catch (error) {
    console.error('更新失败', error)
  }
}

const triggerAvatarUpload = () => {
  avatarInput.value.click()
}

const handleAvatarSelect = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件!')
    return
  }

  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过 10MB!')
    return
  }

  try {
    const res = await uploadFile(file)
    console.log('=== 上传响应 ===', res)
    console.log('=== 图片URL ===', res.data.url)
    
    userForm.avatar = res.data.url
    ElMessage.success('头像上传成功')
    
    console.log('=== userForm.avatar ===', userForm.avatar)
  } catch (error) {
    console.error('头像上传失败', error)
    ElMessage.error('头像上传失败')
  }

  event.target.value = ''
}

onMounted(() => {
  if (!userStore.token) {
    router.push('/login')
    return
  }
  loadUserInfo()
})
</script>

<style scoped>
.el-header {
  background-color: #409eff;
  color: white;
  display: flex;
  align-items: center;
  padding: 0 20px;
}

.header-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.header-content h1 {
  margin: 0;
  font-size: 24px;
  cursor: pointer;
}

.el-main {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.card-header {
  font-weight: bold;
  font-size: 16px;
  text-align: left;
  width: 100%;
}

.profile-form {
  width: 100%;
}

.form-item {
  margin-bottom: 22px;
}

.profile-form :deep(.el-form-item) {
  flex-direction: column;
  align-items: flex-start;
}

.profile-form :deep(.el-form-item__label) {
  padding-right: 0;
  margin-bottom: 8px;
  width: 100%;
  text-align: left;
}

.profile-form :deep(.el-form-item__content) {
  width: 100%;
}

.avatar-section {
  display: flex;
  align-items: flex-start;
  gap: 30px;
}

.user-avatar {
  flex-shrink: 0;
}

.avatar-upload {
  flex: 1;
}

.upload-btn {
  margin-bottom: 10px;
}

.avatar-input {
  width: 100%;
  max-width: 300px;
}

.save-btn {
  width: 100%;
  max-width: 200px;
}

@media (max-width: 768px) {
  .el-header {
    padding: 10px 15px;
  }

  .header-content {
    flex-direction: row;
    align-items: center;
  }

  .header-content h1 {
    font-size: 20px;
  }

  .el-main {
    padding: 15px;
    max-width: 100%;
  }

  .profile-form :deep(.el-form-item) {
    margin-bottom: 18px;
  }

  .profile-form :deep(.el-form-item__label) {
    font-size: 14px;
  }

  .avatar-section {
    flex-direction: column;
    align-items: center;
    gap: 20px;
  }

  .avatar-upload {
    width: 100%;
    text-align: center;
  }

  .upload-btn {
    width: 100%;
    margin-bottom: 15px;
  }

  .avatar-input {
    max-width: 100%;
  }

  .save-btn {
    width: 100%;
    max-width: 100%;
  }
}

@media (max-width: 480px) {
  .header-content h1 {
    font-size: 18px;
  }

  .el-main {
    padding: 10px;
  }

  .profile-form :deep(.el-form-item) {
    margin-bottom: 16px;
  }

  .profile-form :deep(.el-form-item__label) {
    font-size: 13px;
  }

  .user-avatar {
    width: 80px !important;
    height: 80px !important;
  }

  .avatar-section {
    gap: 15px;
  }
}
</style>
