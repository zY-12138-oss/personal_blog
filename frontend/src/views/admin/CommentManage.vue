<template>
  <div class="comment-manage">
    <h2>评论管理</h2>
    
    <div class="desktop-table">
      <el-table :data="comments" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户" width="120" />
        <el-table-column prop="articleTitle" label="文章" show-overflow-tooltip />
        <el-table-column prop="content" label="内容" show-overflow-tooltip />
        <el-table-column prop="likes" label="点赞" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'approved' ? 'success' : 'warning'">
              {{ row.status === 'approved' ? '已审核' : '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="danger" @click="deleteCommentHandle(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="mobile-cards">
      <div v-for="comment in comments" :key="comment.id" class="comment-card">
        <div class="comment-card-header">
          <span class="comment-username">{{ comment.username }}</span>
          <div class="comment-actions">
            <el-button circle size="small" type="danger" @click="deleteCommentHandle(comment.id)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </div>
        </div>
        <div class="comment-card-body">
          <div class="comment-info-item" v-if="comment.articleTitle">
            <span class="label">文章:</span>
            <span class="value">{{ comment.articleTitle }}</span>
          </div>
          <div class="comment-info-item">
            <span class="label">内容:</span>
            <span class="value">{{ comment.content }}</span>
          </div>
          <div class="comment-info-footer">
            <span class="stat-item">
              <el-icon><Star /></el-icon>
              {{ comment.likes }}
            </span>
            <el-tag :type="comment.status === 'approved' ? 'success' : 'warning'" size="small">
              {{ comment.status === 'approved' ? '已审核' : '待审核' }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>

    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="loadComments"
      style="margin-top: 20px; justify-content: flex-end"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Star } from '@element-plus/icons-vue'
import { getCommentList, deleteComment } from '@/api/admin'

const comments = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadComments = async () => {
  loading.value = true
  try {
    const res = await getCommentList({ page: currentPage.value, size: pageSize.value })
    comments.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载评论失败', error)
  } finally {
    loading.value = false
  }
}

const deleteCommentHandle = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteComment(id)
    ElMessage.success('删除成功')
    loadComments()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除评论失败', error)
    }
  }
}

onMounted(() => {
  loadComments()
})
</script>

<style scoped>
.comment-manage h2 {
  margin-bottom: 20px;
  color: #303133;
}

.mobile-cards {
  display: none;
}

.comment-card {
  background: white;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.comment-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.comment-username {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.comment-actions {
  display: flex;
  gap: 8px;
}

.comment-card-body {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.comment-info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.comment-info-item .label {
  font-size: 12px;
  color: #909399;
}

.comment-info-item .value {
  font-size: 14px;
  color: #303133;
  word-break: break-all;
}

.comment-info-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #606266;
}

@media (max-width: 768px) {
  .desktop-table {
    display: none;
  }

  .mobile-cards {
    display: block;
  }

  .comment-manage h2 {
    font-size: 18px;
  }
}
</style>
