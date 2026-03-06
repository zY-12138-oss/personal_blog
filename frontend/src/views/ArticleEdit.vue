<template>
  <div class="article-edit">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1 @click="$router.push('/articles')" style="cursor: pointer">{{ isEdit ? '编辑文章' : '写文章' }}</h1>
          <div class="nav-buttons">
            <el-button @click="$router.back()">取消</el-button>
            <el-button type="primary" @click="handleSave('draft')">保存草稿</el-button>
            <el-button type="success" @click="handleSave('published')">发布</el-button>
          </div>
        </div>
      </el-header>
      <el-main>
        <el-form :model="articleForm" label-width="80px">
          <el-form-item label="标题">
            <el-input v-model="articleForm.title" placeholder="请输入文章标题" />
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="articleForm.categoryId" placeholder="请选择分类" clearable>
              <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="摘要">
            <el-input v-model="articleForm.summary" type="textarea" :rows="3" placeholder="请输入文章摘要" />
          </el-form-item>
          <el-form-item label="内容">
            <el-input v-model="articleForm.content" type="textarea" :rows="15" placeholder="请输入文章内容" />
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createArticle, updateArticle, getArticle } from '@/api/article'
import { getCategoryList } from '@/api/category'

const router = useRouter()
const route = useRoute()

const isEdit = ref(false)
const articleId = ref(null)
const categories = ref([])

const articleForm = reactive({
  title: '',
  content: '',
  summary: '',
  categoryId: null
})

const loadCategories = async () => {
  try {
    const res = await getCategoryList()
    categories.value = res.data
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const loadArticle = async (id) => {
  try {
    const res = await getArticle(id)
    articleForm.title = res.data.title
    articleForm.content = res.data.content
    articleForm.summary = res.data.summary
    articleForm.categoryId = res.data.categoryId
  } catch (error) {
    console.error('加载文章失败', error)
  }
}

const handleSave = async (status) => {
  if (!articleForm.title) {
    ElMessage.error('请输入标题')
    return
  }
  if (!articleForm.content) {
    ElMessage.error('请输入内容')
    return
  }

  try {
    if (isEdit.value) {
      await updateArticle(articleId.value, { ...articleForm, status })
      ElMessage.success('更新成功')
    } else {
      await createArticle({ ...articleForm, status })
      ElMessage.success(status === 'published' ? '发布成功' : '保存成功')
    }
    router.push('/articles')
  } catch (error) {
    console.error('保存失败', error)
  }
}

onMounted(() => {
  loadCategories()
  if (route.params.id) {
    isEdit.value = true
    articleId.value = route.params.id
    loadArticle(route.params.id)
  }
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
  cursor: pointer;
}

.el-main {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}
</style>
