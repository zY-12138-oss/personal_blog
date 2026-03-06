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
          <el-form :model="userForm" label-width="80px">
            <el-form-item label="用户名">
              <el-input v-model="userForm.username" disabled />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="userForm.email" disabled />
            </el-form-item>
            <el-form-item label="简介">
              <el-input v-model="userForm.bio" type="textarea" :rows="4" />
            </el-form-item>
            <el-form-item label="头像">
              <el-input v-model="userForm.avatar" placeholder="头像 URL" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleUpdate">保存修改</el-button>
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
import { jwtDecode } from 'jwt-decode'
import { useUserStore } from '@/stores/user'
import { getUserInfo, updateUserInfo } from '@/api/user'

const router = useRouter()
const userStore = useUserStore()

const userForm = reactive({
  username: '',
  email: '',
  bio: '',
  avatar: ''
})

const userId = ref(null)

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
}
</style>
