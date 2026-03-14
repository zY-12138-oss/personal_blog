<template>
  <div class="user-manage">
    <h2>用户管理</h2>
    
    <div class="desktop-table">
      <el-table :data="users" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="bio" label="简介" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="注册时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button size="small" type="danger" @click="deleteUserHandle(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="mobile-cards">
      <div v-for="user in users" :key="user.id" class="user-card">
        <div class="user-card-header">
          <span class="user-name">{{ user.username }}</span>
          <el-button circle size="small" type="danger" @click="deleteUserHandle(user.id)">
            <el-icon><Delete /></el-icon>
          </el-button>
        </div>
        <div class="user-card-body">
          <div class="user-info-item">
            <span class="label">ID:</span>
            <span class="value">{{ user.id }}</span>
          </div>
          <div class="user-info-item">
            <span class="label">邮箱:</span>
            <span class="value">{{ user.email }}</span>
          </div>
          <div class="user-info-item" v-if="user.bio">
            <span class="label">简介:</span>
            <span class="value">{{ user.bio }}</span>
          </div>
          <div class="user-info-item">
            <span class="label">注册时间:</span>
            <span class="value">{{ formatDate(user.createdAt) }}</span>
          </div>
        </div>
      </div>
    </div>

    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="loadUsers"
      style="margin-top: 20px; justify-content: flex-end"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import { getUserList, deleteUser } from '@/api/admin'

const users = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadUsers = async () => {
  loading.value = true
  try {
    const res = await getUserList({ page: currentPage.value, size: pageSize.value })
    users.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('加载用户失败', error)
  } finally {
    loading.value = false
  }
}

const deleteUserHandle = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个用户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteUser(id)
    ElMessage.success('删除成功')
    loadUsers()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除用户失败', error)
    }
  }
}

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleString('zh-CN')
}

onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.user-manage h2 {
  margin-bottom: 20px;
  color: #303133;
}

.mobile-cards {
  display: none;
}

.user-card {
  background: white;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.user-name {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.user-card-body {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.user-info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-info-item .label {
  font-size: 12px;
  color: #909399;
}

.user-info-item .value {
  font-size: 14px;
  color: #303133;
  word-break: break-all;
}

@media (max-width: 768px) {
  .desktop-table {
    display: none;
  }

  .mobile-cards {
    display: block;
  }

  .user-manage h2 {
    font-size: 18px;
  }
}
</style>
