<template>
  <div class="article-manage">
    <div class="page-header">
      <h2>文章管理</h2>
      <el-button type="primary" class="btn-desktop" @click="$router.push('/article/edit')">新建文章</el-button>
      <el-button type="primary" class="btn-mobile" circle :icon="Plus" @click="$router.push('/article/edit')" />
    </div>
    
    <div class="desktop-table">
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
            <el-button size="small" type="danger" @click="deleteArticleHandle(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="mobile-cards">
      <div v-for="article in articles" :key="article.id" class="article-card">
        <div class="article-card-header">
          <span class="article-title">{{ article.title }}</span>
          <div class="article-actions">
            <el-button circle size="small" @click="viewArticle(article.id)">
              <el-icon><View /></el-icon>
            </el-button>
            <el-button circle size="small" type="primary" @click="editArticle(article.id)">
              <el-icon><Edit /></el-icon>
            </el-button>
            <el-button circle size="small" type="danger" @click="deleteArticleHandle(article.id)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </div>
        </div>
        <div class="article-card-body">
          <div class="article-info-item">
            <span class="label">ID:</span>
            <span class="value">{{ article.id }}</span>
          </div>
          <div class="article-info-item" v-if="article.authorName">
            <span class="label">作者:</span>
            <span class="value">{{ article.authorName }}</span>
          </div>
          <div class="article-info-item" v-if="article.categoryName">
            <span class="label">分类:</span>
            <span class="value">{{ article.categoryName }}</span>
          </div>
          <div class="article-info-stats">
            <span class="stat-item">
              <el-icon><View /></el-icon>
              {{ article.views }}
            </span>
            <span class="stat-item">
              <el-icon><Star /></el-icon>
              {{ article.likes }}
            </span>
          </div>
          <div class="article-status">
            <el-tag :type="article.status === 'published' ? 'success' : 'info'" size="small">
              {{ article.status === 'published' ? '已发布' : '草稿' }}
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
      @current-change="loadArticles"
      style="margin-top: 20px; justify-content: flex-end"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, View, Edit, Delete, Star } from '@element-plus/icons-vue'
import { getArticleList, deleteArticle } from '@/api/admin'

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

.btn-desktop {
  display: inline-flex;
}

.btn-mobile {
  display: none;
}

.mobile-cards {
  display: none;
}

.article-card {
  background: white;
  border-radius: 8px;
  padding: 15px;
  margin-bottom: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.article-card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.article-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  flex: 1;
  margin-right: 10px;
  word-break: break-all;
}

.article-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.article-card-body {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.article-info-item {
  display: flex;
  gap: 8px;
}

.article-info-item .label {
  font-size: 12px;
  color: #909399;
}

.article-info-item .value {
  font-size: 14px;
  color: #303133;
}

.article-info-stats {
  display: flex;
  gap: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #606266;
}

.article-status {
  display: flex;
  justify-content: flex-start;
}

@media (max-width: 768px) {
  .desktop-table {
    display: none;
  }

  .mobile-cards {
    display: block;
  }

  .btn-desktop {
    display: none;
  }

  .btn-mobile {
    display: inline-flex;
  }

  .page-header h2 {
    font-size: 18px;
  }
}
</style>
