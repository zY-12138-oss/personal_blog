<template>
  <div class="comment-manage">
    <h2>评论管理</h2>
    <el-table :data="comments" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户" width="120" />
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
          <el-button size="small" type="danger" @click="deleteComment(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
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
import { getCommentsByArticle, deleteComment } from '@/api/comment'

const comments = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadComments = async () => {
  loading.value = true
  try {
    const res = await getCommentsByArticle(0)
    comments.value = res.data || []
    total.value = comments.value.length
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
</style>
