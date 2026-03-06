<template>
  <div class="dashboard">
    <h2>仪表板</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ stats.userCount }}</div>
            <div class="stat-label">用户总数</div>
          </div>
          <el-icon class="stat-icon" :size="40" color="#409eff"><User /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ stats.articleCount }}</div>
            <div class="stat-label">文章总数</div>
          </div>
          <el-icon class="stat-icon" :size="40" color="#67c23a"><Document /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ stats.publishedArticleCount }}</div>
            <div class="stat-label">已发布</div>
          </div>
          <el-icon class="stat-icon" :size="40" color="#e6a23c"><SuccessFilled /></el-icon>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ stats.commentCount }}</div>
            <div class="stat-label">评论总数</div>
          </div>
          <el-icon class="stat-icon" :size="40" color="#f56c6c"><ChatDotRound /></el-icon>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStats } from '@/api/admin'
import { User, Document, SuccessFilled, ChatDotRound } from '@element-plus/icons-vue'

const stats = ref({
  userCount: 0,
  articleCount: 0,
  publishedArticleCount: 0,
  commentCount: 0
})

const loadStats = async () => {
  try {
    const res = await getStats()
    stats.value = res.data
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard h2 {
  margin-bottom: 20px;
  color: #303133;
}

.stat-card {
  margin-bottom: 20px;
}

.stat-card .el-card__body {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-content {
  text-align: left;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.stat-label {
  color: #909399;
  font-size: 14px;
}

.stat-icon {
  opacity: 0.3;
}
</style>
