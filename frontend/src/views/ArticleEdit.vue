<template>
  <div class="article-edit">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1 @click="$router.push('/articles')" style="cursor: pointer">{{ isEdit ? '编辑文章' : '写文章' }}</h1>
          <div class="nav-buttons">
            <el-button class="btn-desktop" @click="$router.back()">取消</el-button>
            <el-button class="btn-mobile" circle :icon="Close" @click="$router.back()" />
            
            <el-button type="primary" class="btn-desktop" @click="handleSave('draft')">保存草稿</el-button>
            <el-button type="primary" class="btn-mobile" circle :icon="Document" @click="handleSave('draft')" />
            
            <el-button type="success" class="btn-desktop" @click="handleSave('published')">发布</el-button>
            <el-button type="success" class="btn-mobile" circle :icon="Upload" @click="handleSave('published')" />
          </div>
        </div>
      </el-header>
      <el-main>
        <el-form :model="articleForm" label-width="80px" class="article-edit-form">
          <el-form-item label="标题">
            <el-input v-model="articleForm.title" placeholder="请输入文章标题" />
          </el-form-item>

          <el-form-item label="摘要">
            <el-input v-model="articleForm.summary" type="textarea" :rows="3" placeholder="请输入文章摘要" />
          </el-form-item>
          <el-form-item label="内容">
            <div class="content-editor">
              <div class="editor-toolbar">
                <input
                  type="file"
                  ref="imageInput"
                  @change="handleImageSelect"
                  accept="image/*"
                  style="display: none"
                />
                <el-button type="primary" :icon="Picture" @click="triggerImageUpload">插入图片</el-button>
              </div>
              <el-input v-model="articleForm.content" type="textarea" :rows="15" placeholder="请输入文章内容，支持Markdown格式" />
            </div>
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
import { Picture, Check, Close, Document, Upload } from '@element-plus/icons-vue'
import { createArticle, updateArticle, getArticle } from '@/api/article'
import { uploadFile } from '@/api/file'

const router = useRouter()
const route = useRoute()

const isEdit = ref(false)
const articleId = ref(null)

const articleForm = reactive({
  title: '',
  content: '',
  summary: ''
})

const imageInput = ref(null)

const loadArticle = async (id) => {
  try {
    const res = await getArticle(id)
    articleForm.title = res.data.title
    articleForm.content = res.data.content
    articleForm.summary = res.data.summary
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

const triggerImageUpload = () => {
  imageInput.value.click()
}

const handleImageSelect = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件!')
    return
  }

  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过 10MB!')
    return
  }

  try {
    const res = await uploadFile(file)
    const imageUrl = res.data.url
    const markdownImage = `![图片](${imageUrl})\n`
    articleForm.content = articleForm.content + markdownImage
    ElMessage.success('图片已插入')
  } catch (error) {
    console.error('图片上传失败', error)
    ElMessage.error('图片上传失败')
  }

  event.target.value = ''
}

onMounted(() => {
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
  flex-wrap: wrap;
  gap: 10px;
}

.header-content h1 {
  margin: 0;
  font-size: 24px;
  cursor: pointer;
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

.el-main {
  max-width: 100%;
  width: 95%;
  margin: 0 auto;
  padding: 20px;
}

.article-edit-form {
  width: 100%;
}

.content-editor {
  width: 100%;
}

.editor-toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  flex-wrap: wrap;
  gap: 10px;
}

.image-url-input {
  flex: 1;
  min-width: 200px;
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

  .el-main {
    padding: 15px;
    max-width: 900px;
  }

  .editor-toolbar {
    flex-direction: column;
    align-items: stretch;
  }

  .editor-toolbar .el-button {
    width: 100%;
  }

  .image-url-input {
    width: 100%;
    min-width: auto;
  }
}

@media (max-width: 480px) {
  .header-content h1 {
    font-size: 18px;
  }

  .el-main {
    padding: 10px;
  }

  .article-edit-form :deep(.el-form-item__label) {
    font-size: 14px;
  }
}
</style>
