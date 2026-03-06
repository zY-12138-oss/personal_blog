<template>
  <div class="home">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>个人博客</h1>
          <div class="nav-buttons">
            <el-button v-if="!userStore.token" @click="$router.push('/login')">登录</el-button>
            <el-button v-if="!userStore.token" type="primary" @click="$router.push('/register')">注册</el-button>
            <el-button v-else @click="handleLogout">退出登录</el-button>
          </div>
        </div>
      </el-header>
      <el-main>
        <div class="welcome">
          <h2>欢迎来到个人博客</h2>
          <p>这是一个基于 Vue3 + Spring Boot 的全栈个人博客系统</p>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

const handleLogout = () => {
  userStore.logout()
  router.push('/')
}
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
}

.el-main {
  padding: 40px;
  text-align: center;
}

.welcome h2 {
  font-size: 32px;
  margin-bottom: 20px;
  color: #303133;
}

.welcome p {
  font-size: 18px;
  color: #606266;
}
</style>
