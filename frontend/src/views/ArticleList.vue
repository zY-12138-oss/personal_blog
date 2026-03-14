<template>
  <div class="article-list">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1 @click="$router.push('/')" style="cursor: pointer">个人博客</h1>
          <div class="nav-buttons">
            <template v-if="userStore.token">
              <el-button v-if="userStore.role === 'ADMIN'" class="btn-desktop" @click="$router.push('/admin/dashboard')">管理后台</el-button>
              <el-button v-if="userStore.role === 'ADMIN'" class="btn-mobile" circle :icon="Setting" @click="$router.push('/admin/dashboard')" />
              
              <el-button class="btn-desktop" @click="$router.push('/profile')">个人中心</el-button>
              <el-button class="btn-mobile" circle :icon="User" @click="$router.push('/profile')" />
              
              <el-button type="primary" class="btn-desktop" @click="$router.push('/article/edit')">写文章</el-button>
              <el-button type="primary" class="btn-mobile" circle :icon="Edit" @click="$router.push('/article/edit')" />
              
              <el-button class="btn-desktop" @click="handleLogout">退出登录</el-button>
              <el-button class="btn-mobile" circle :icon="SwitchButton" @click="handleLogout" />
            </template>
            <template v-else>
              <el-button class="btn-desktop" @click="$router.push('/login')">登录</el-button>
              <el-button class="btn-mobile" circle :icon="Lock" @click="$router.push('/login')" />
              
              <el-button type="primary" class="btn-desktop" @click="$router.push('/register')">注册</el-button>
              <el-button type="primary" class="btn-mobile" circle :icon="Plus" @click="$router.push('/register')" />
            </template>
          </div>
        </div>
      </el-header>
      <el-main class="main-container">
        <div class="content-wrapper">
          <div class="main-content">
            <el-card class="search-card">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索文章..."
                clearable
                @keyup.enter="handleSearch"
              >
                <template #append>
                  <el-button icon="Search" @click="handleSearch">搜索</el-button>
                </template>
              </el-input>
            </el-card>

            <el-card v-for="article in articles" :key="article.id" class="article-card" @click="viewArticle(article.id)">
              <h2 class="article-title">{{ article.title }}</h2>
              <div class="article-meta">
                <span>作者: {{ article.authorName }}</span>
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
              @current-change="handlePageChange"
            />
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getArticleList, searchArticles } from '@/api/article'
import { User, Edit, SwitchButton, Lock, Plus, Setting } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const articles = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchKeyword = ref('')
const isSearching = ref(false)

const loadArticles = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value
    }
    const res = await getArticleList(params)
    articles.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('加载文章失败', error)
  }
}

const handleSearch = async () => {
  currentPage.value = 1
  if (!searchKeyword.value.trim()) {
    isSearching.value = false
    loadArticles()
    return
  }
  isSearching.value = true
  try {
    const res = await searchArticles({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchKeyword.value
    })
    articles.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('搜索失败', error)
  }
}

const handlePageChange = () => {
  if (isSearching.value) {
    handleSearch()
  } else {
    loadArticles()
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

.main-container {
  padding: 20px;
}

.content-wrapper {
  max-width: 900px;
  margin: 0 auto;
}

.main-content {
  width: 100%;
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
  flex-wrap: wrap;
}

.article-summary {
  color: #606266;
  margin: 0;
}

.el-pagination {
  margin-top: 30px;
  justify-content: center;
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

  .main-container {
    padding: 15px;
  }

  .article-title {
    font-size: 18px;
  }

  .article-meta {
    gap: 10px;
    font-size: 12px;
  }

  .el-pagination {
    font-size: 12px;
  }
}

@media (max-width: 480px) {
  .header-content h1 {
    font-size: 18px;
  }

  .article-title {
    font-size: 16px;
  }

  .article-meta {
    gap: 8px;
    font-size: 11px;
  }
}
</style>
