<template>
  <div class="login-container">
    <div class="login-box">
      <h2>个人博客系统</h2>
      <el-tabs v-model="activeTab" class="login-tabs">
        <el-tab-pane label="用户登录" name="user">
          <el-form :model="userLoginForm" :rules="rules" ref="userLoginFormRef" label-width="0">
            <el-form-item prop="username">
              <el-input v-model="userLoginForm.username" placeholder="用户名" prefix-icon="User" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="userLoginForm.password" type="password" placeholder="密码" prefix-icon="Lock" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" style="width: 100%" @click="handleUserLogin" :loading="loading">
                用户登录
              </el-button>
            </el-form-item>
          </el-form>
          <div class="register-link">
            还没有账号？<router-link to="/register">立即注册</router-link>
          </div>
        </el-tab-pane>
        <el-tab-pane label="管理员登录" name="admin">
          <el-form :model="adminLoginForm" :rules="rules" ref="adminLoginFormRef" label-width="0">
            <el-form-item prop="username">
              <el-input v-model="adminLoginForm.username" placeholder="管理员用户名" prefix-icon="User" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="adminLoginForm.password" type="password" placeholder="密码" prefix-icon="Lock" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="danger" style="width: 100%" @click="handleAdminLogin" :loading="loading">
                管理员登录
              </el-button>
            </el-form-item>
          </el-form>
          <div class="admin-tip">
            <el-alert
              title="默认管理员账号"
              type="info"
              :closable="false"
              description="用户名: admin / 密码: admin123"
              show-icon
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/user'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('user')
const loading = ref(false)
const userLoginFormRef = ref(null)
const adminLoginFormRef = ref(null)

const userLoginForm = reactive({
  username: '',
  password: ''
})

const adminLoginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleUserLogin = async () => {
  if (!userLoginFormRef.value) return
  await userLoginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await login(userLoginForm)
        if (res.data.role === 'ADMIN') {
          ElMessage.error('请使用管理员登录入口')
          loading.value = false
          return
        }
        userStore.setToken(res.data.token)
        userStore.setRole(res.data.role)
        userStore.setUserInfo({
          id: res.data.userId,
          username: res.data.username
        })
        ElMessage.success('登录成功')
        router.push('/')
      } catch (error) {
        console.error('登录失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}

const handleAdminLogin = async () => {
  if (!adminLoginFormRef.value) return
  await adminLoginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await login(adminLoginForm)
        if (res.data.role !== 'ADMIN') {
          ElMessage.error('该账号不是管理员账号')
          loading.value = false
          return
        }
        userStore.setToken(res.data.token)
        userStore.setRole(res.data.role)
        userStore.setUserInfo({
          id: res.data.userId,
          username: res.data.username
        })
        ElMessage.success('管理员登录成功')
        router.push('/admin')
      } catch (error) {
        console.error('登录失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}

.login-tabs {
  margin-top: 20px;
}

.register-link {
  text-align: center;
  margin-top: 15px;
}

.register-link a {
  color: #409eff;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}

.admin-tip {
  margin-top: 20px;
}
</style>
