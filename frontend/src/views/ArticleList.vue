<template>
  <div class="article-list">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1 @click="$router.push('/')" style="cursor: pointer">个人博客</h1>
          <div class="nav-buttons">
            <el-button v-if="userStore.token && userStore.role === 'ADMIN'" @click="$router.push('/admin/dashboard')">管理后台</el-button>
            <el-button v-if="userStore.token" @click="$router.push('/profile')">个人中心</el-button>
            <el-button v-if="userStore.token" type="primary" @click="$router.push('/article/edit')">写文章</el-button>
            <el-button v-if="!userStore.token" @click="$router.push('/login')">登录</el-button>
            <el-button v-if="!userStore.token" type="primary" @click="$router.push('/register')">注册</el-button>
            <el-button v-else @click="handleLogout">退出登录</el-button>
          </div>
        </div>
      </el-header>
      <el-main>
        <el-card class="search-card">
          <el-select v-model="selectedCategory" placeholder="选择分类" clearable @change="loadArticles">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-card>

        <el-card v-for="article in articles" :key="article.id" class="article-card" @click="viewArticle(article.id)">
          <h2 class="article-title">{{ article.title }}</h2>
          <div class="article-meta">
            <span>作者: {{ article.authorName }}</span>
            <span v-if="article.categoryName">分类: {{ article.categoryName }}</span>
            <span>浏览: {{ article.views }}</span>
            <span>点赞: {{ article.likes }}</span>
            <span>{{ formatDate(article.createdAt) }}</span>
          </div>
          <p class="article-summary">{{ article.summary }}</p>
        </el-card>

        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="loadArticles"
        />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getArticleList } from '@/api/article'
import { getCategoryList } from '@/api/category'

const router = useRouter()
const userStore = useUserStore()

const articles = ref([])
const categories = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const selectedCategory = ref(null)

const loadArticles = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value
    }
    if (selectedCategory.value) {
      params.categoryId = selectedCategory.value
    }
    const res = await getArticleList(params)
    articles.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('加载文章失败', error)
  }
}

const loadCategories = async () => {
  try {
    const res = await getCategoryList()
    categories.value = res.data
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const viewArticle = (id) => {
  router.push(`/article/${id}`)
}

const handleLogout = () => {
  userStore.logout()
  router.push('/')
}

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadArticles()
  loadCategories()
})
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
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.article-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: box-shadow 0.3s;
}

.article-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.article-title {
  margin: 0 0 10px 0;
  font-size: 22px;
  color: #303133;
}

.article-meta {
  display: flex;
  gap: 20px;
  color: #909399;
  font-size: 14px;
  margin-bottom: 10px;
}

.article-summary {
  color: #606266;
  margin: 0;
}

.el-pagination {
  margin-top: 30px;
  justify-content: center;
}
</style>
