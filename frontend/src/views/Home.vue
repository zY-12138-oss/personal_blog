<template>
  <div class="home">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1>个人博客</h1>
          <div class="nav-buttons">
            <template v-if="!userStore.token">
              <el-button class="btn-desktop" @click="$router.push('/login')">登录</el-button>
              <el-button class="btn-mobile" circle :icon="Lock" @click="$router.push('/login')" />
              <el-button type="primary" class="btn-desktop" @click="$router.push('/register')">注册</el-button>
              <el-button type="primary" class="btn-mobile" circle :icon="Plus" @click="$router.push('/register')" />
            </template>
            <template v-else>
              <el-button v-if="userStore.role === 'ADMIN'" class="btn-desktop" @click="$router.push('/admin/dashboard')">管理后台</el-button>
              <el-button v-if="userStore.role === 'ADMIN'" class="btn-mobile" circle :icon="Setting" @click="$router.push('/admin/dashboard')" />
              
              <el-button class="btn-desktop" @click="handleLogout">退出登录</el-button>
              <el-button class="btn-mobile" circle :icon="SwitchButton" @click="handleLogout" />
            </template>
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
import { Lock, Plus, SwitchButton, Setting } from '@element-plus/icons-vue'

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
  flex-wrap: wrap;
  gap: 10px;
}

.header-content h1 {
  margin: 0;
  font-size: 24px;
}

.nav-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.btn-desktop {
  display: inline-flex;
}

.btn-mobile {
  display: none;
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

  .nav-buttons {
    width: auto;
    justify-content: flex-end;
  }

  .btn-desktop {
    display: none;
  }

  .btn-mobile {
    display: inline-flex;
  }

  .el-main {
    padding: 30px 20px;
  }

  .welcome h2 {
    font-size: 24px;
  }

  .welcome p {
    font-size: 16px;
  }
}

@media (max-width: 480px) {
  .header-content h1 {
    font-size: 18px;
  }

  .welcome h2 {
    font-size: 20px;
  }

  .welcome p {
    font-size: 14px;
  }
}
</style>
