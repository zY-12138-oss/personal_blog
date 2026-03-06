<template>
  <div class="article-manage">
    <div class="page-header">
      <h2>文章管理</h2>
      <el-button type="primary" @click="$router.push('/article/edit')">新建文章</el-button>
    </div>
    <el-table :data="articles" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="authorName" label="作者" width="120" />
      <el-table-column prop="categoryName" label="分类" width="100" />
      <el-table-column prop="views" label="浏览" width="80" />
      <el-table-column prop="likes" label="点赞" width="80" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'published' ? 'success' : 'info'">
            {{ row.status === 'published' ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="viewArticle(row.id)">查看</el-button>
          <el-button size="small" type="primary" @click="editArticle(row.id)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteArticle(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="loadArticles"
      style="margin-top: 20px; justify-content: flex-end"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getArticleList, deleteArticle } from '@/api/article'

const router = useRouter()
const articles = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadArticles = async () => {
  loading.value = true
  try {
    const res = await getArticleList({ page: currentPage.value, size: pageSize.value })
    articles.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('加载文章失败', error)
  } finally {
    loading.value = false
  }
}

const viewArticle = (id) => {
  router.push(`/article/${id}`)
}

const editArticle = (id) => {
  router.push(`/article/edit/${id}`)
}

const deleteArticleHandle = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这篇文章吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteArticle(id)
    ElMessage.success('删除成功')
    loadArticles()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除文章失败', error)
    }
  }
}

onMounted(() => {
  loadArticles()
})
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
}
</style>
