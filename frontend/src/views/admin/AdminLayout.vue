<template>
  <el-container class="admin-layout">
    <el-aside width="200px" class="sidebar-desktop">
      <div class="logo">
        <h2>博客管理</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataLine /></el-icon>
          <span>仪表板</span>
        </el-menu-item>
        <el-menu-item index="/admin/articles">
          <el-icon><Document /></el-icon>
          <span>文章管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/comments">
          <el-icon><ChatDotRound /></el-icon>
          <span>评论管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/profile">
          <el-icon><Setting /></el-icon>
          <span>个人中心</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header class="mobile-header">
        <div class="mobile-nav">
          <el-button circle size="small" @click="$router.push('/admin/dashboard')">
            <el-icon><DataLine /></el-icon>
          </el-button>
          <el-button circle size="small" @click="$router.push('/admin/articles')">
            <el-icon><Document /></el-icon>
          </el-button>
          <el-button circle size="small" @click="$router.push('/admin/comments')">
            <el-icon><ChatDotRound /></el-icon>
          </el-button>
          <el-button circle size="small" @click="$router.push('/admin/users')">
            <el-icon><User /></el-icon>
          </el-button>
        </div>
        <div class="mobile-actions">
          <el-button circle size="small" @click="$router.push('/articles')">
            <el-icon><HomeFilled /></el-icon>
          </el-button>
          <el-button circle size="small" type="danger" v-if="userStore.token" @click="handleLogout">
            <el-icon><SwitchButton /></el-icon>
          </el-button>
        </div>
      </el-header>
      
      <el-header class="desktop-header">
        <div class="header-content">
          <span>欢迎，{{ userStore.token ? '管理员' : '访客' }}</span>
          <div class="header-actions">
            <el-button @click="$router.push('/articles')">返回首页</el-button>
            <el-button v-if="userStore.token" type="danger" @click="handleLogout">退出</el-button>
          </div>
        </div>
      </el-header>
      
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { DataLine, Document, ChatDotRound, User, Setting, HomeFilled, SwitchButton } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
}

.sidebar-desktop {
  background-color: #545c64;
  color: white;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #434a50;
}

.logo h2 {
  margin: 0;
  font-size: 18px;
  color: white;
}

.desktop-header {
  background-color: white;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  padding: 0 20px;
}

.mobile-header {
  background-color: #545c64;
  display: none;
  align-items: center;
  justify-content: space-between;
  padding: 10px 15px;
  height: auto;
}

.mobile-nav {
  display: flex;
  gap: 8px;
}

.mobile-actions {
  display: flex;
  gap: 8px;
}

.header-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-main {
  background-color: #f0f2f5;
  padding: 20px;
}

@media (max-width: 768px) {
  .sidebar-desktop {
    display: none;
  }

  .desktop-header {
    display: none;
  }

  .mobile-header {
    display: flex;
  }

  .el-main {
    padding: 15px;
  }
}
</style>
