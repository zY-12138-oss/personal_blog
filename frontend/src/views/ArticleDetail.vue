<template>
  <div class="article-detail">
    <el-container>
      <el-header>
        <div class="header-content">
          <h1 @click="$router.push('/articles')" style="cursor: pointer">个人博客</h1>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </el-header>
      <el-main v-if="article">
        <el-card class="article-card">
          <h1 class="article-title">{{ article.title }}</h1>
          <div class="article-meta">
            <div class="author-section" @click="showAuthorInfo">
              <el-avatar :size="40" :src="article.authorAvatar" style="cursor: pointer;">
                {{ article.authorName ? article.authorName.charAt(0) : 'U' }}
              </el-avatar>
              <span class="author-name" style="cursor: pointer;">{{ article.authorName }}</span>
            </div>
            <span>浏览: {{ article.views }}</span>
            <span class="like-section">
              <el-button 
                :type="hasLiked ? 'danger' : 'primary'" 
                :icon="hasLiked ? 'StarFilled' : 'Star'"
                @click="handleLike"
                :disabled="!userStore.token"
                circle
                size="small"
              />
              <span class="like-count">{{ article.likes }}</span>
            </span>
            <span>{{ formatDate(article.createdAt) }}</span>
          </div>
          <div class="article-content" v-html="renderContent(article.content)"></div>
        </el-card>

        <el-card class="comment-card">
          <template #header>
            <div class="comment-header">
              <span>评论 ({{ comments.length }})</span>
            </div>
          </template>

          <div v-if="userStore.token" class="comment-input-section">
            <el-input
              v-model="newComment"
              type="textarea"
              :rows="3"
              placeholder="发表您的评论..."
            />
            <div class="comment-actions">
              <el-button type="primary" @click="submitComment" :loading="submitting">发表评论</el-button>
            </div>
          </div>
          <div v-else class="login-tip">
            <el-button type="primary" link @click="$router.push('/login')">登录后发表评论</el-button>
          </div>

          <div class="comment-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <div class="comment-user">
                <el-avatar :size="32" :src="comment.avatar">
                  {{ comment.username ? comment.username.charAt(0) : 'U' }}
                </el-avatar>
                <span class="username">{{ comment.username || '匿名用户' }}</span>
                <span class="comment-time">{{ formatDate(comment.createdAt) }}</span>
              </div>
              <div class="comment-content">{{ comment.content }}</div>
              <div class="comment-footer">
                <span class="like-section">
                  <el-button 
                    :type="commentLikedMap.get(comment.id) ? 'danger' : 'default'" 
                    :icon="commentLikedMap.get(comment.id) ? 'StarFilled' : 'Star'"
                    @click="handleCommentLike(comment)"
                    :disabled="!userStore.token"
                    circle
                    size="small"
                    link
                  />
                  <span class="like-count">{{ comment.likes }}</span>
                </span>
                <el-button
                  v-if="userStore.token && comment.userId === currentUserId"
                  type="danger"
                  link
                  size="small"
                  @click="handleDeleteComment(comment.id)"
                >
                  删除
                </el-button>
              </div>
            </div>
            <el-empty v-if="comments.length === 0" description="暂无评论，快来抢沙发吧！" />
          </div>
        </el-card>
      </el-main>
      <el-main v-else>
        <el-empty description="加载中..." />
      </el-main>
    </el-container>

    <el-dialog v-model="authorDialogVisible" title="作者信息" width="500px">
      <div v-if="authorInfo" class="author-info-dialog">
        <div class="author-avatar-section">
          <el-avatar :size="100" :src="authorInfo.avatar">
            {{ authorInfo.username ? authorInfo.username.charAt(0) : 'U' }}
          </el-avatar>
        </div>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="用户名">
            {{ authorInfo.username }}
          </el-descriptions-item>
          <el-descriptions-item label="邮箱">
            {{ authorInfo.email }}
          </el-descriptions-item>
          <el-descriptions-item label="简介">
            {{ authorInfo.bio || '暂无简介' }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getArticle, likeArticle, unlikeArticle, hasLikedArticle } from '@/api/article'
import { getCommentsByArticle, createComment, deleteComment, likeComment, unlikeComment, hasLikedComment } from '@/api/comment'
import { getUserInfo } from '@/api/user'
import { useUserStore } from '@/stores/user'
import { jwtDecode } from 'jwt-decode'

const route = useRoute()
const userStore = useUserStore()

const article = ref(null)
const comments = ref([])
const newComment = ref('')
const submitting = ref(false)
const hasLiked = ref(false)
const commentLikedMap = ref(new Map())
const authorDialogVisible = ref(false)
const authorInfo = ref(null)

const currentUserId = computed(() => {
  if (userStore.token) {
    try {
      const decoded = jwtDecode(userStore.token)
      return parseInt(decoded.sub)
    } catch (e) {
      return null
    }
  }
  return null
})

const loadArticle = async () => {
  try {
    const res = await getArticle(route.params.id)
    article.value = res.data
    if (userStore.token) {
      checkHasLiked()
    }
  } catch (error) {
    console.error('加载文章失败', error)
  }
}

const checkHasLiked = async () => {
  try {
    const res = await hasLikedArticle(route.params.id)
    hasLiked.value = res.data
  } catch (error) {
    console.error('检查点赞状态失败', error)
  }
}

const handleLike = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    return
  }
  try {
    if (hasLiked.value) {
      await unlikeArticle(route.params.id)
      hasLiked.value = false
      article.value.likes--
      ElMessage.success('取消点赞')
    } else {
      await likeArticle(route.params.id)
      hasLiked.value = true
      article.value.likes++
      ElMessage.success('点赞成功')
    }
  } catch (error) {
    console.error('点赞失败', error)
  }
}

const loadComments = async () => {
  try {
    const res = await getCommentsByArticle(route.params.id)
    comments.value = res.data
    if (userStore.token) {
      checkAllCommentsLiked()
    }
  } catch (error) {
    console.error('加载评论失败', error)
  }
}

const checkAllCommentsLiked = async () => {
  for (const comment of comments.value) {
    try {
      const res = await hasLikedComment(comment.id)
      commentLikedMap.value.set(comment.id, res.data)
    } catch (error) {
      console.error('检查评论点赞状态失败', error)
    }
  }
}

const handleCommentLike = async (comment) => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    return
  }
  const commentId = comment.id
  const isLiked = commentLikedMap.value.get(commentId)
  try {
    if (isLiked) {
      await unlikeComment(commentId)
      commentLikedMap.value.set(commentId, false)
      comment.likes--
      ElMessage.success('取消点赞')
    } else {
      await likeComment(commentId)
      commentLikedMap.value.set(commentId, true)
      comment.likes++
      ElMessage.success('点赞成功')
    }
  } catch (error) {
    console.error('评论点赞失败', error)
  }
}

const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  
  submitting.value = true
  try {
    await createComment({
      articleId: route.params.id,
      content: newComment.value
    })
    ElMessage.success('评论成功')
    newComment.value = ''
    loadComments()
  } catch (error) {
    console.error('发表评论失败', error)
  } finally {
    submitting.value = false
  }
}

const handleDeleteComment = async (id) => {
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

const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleString('zh-CN')
}

const renderContent = (content) => {
  if (!content) return ''
  
  let html = content
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/\n/g, '<br>')
  
  html = html.replace(/!\[([^\]]*)\]\(([^)]+)\)/g, '<img src="$2" alt="$1" style="max-width: 100%; height: auto; margin: 10px 0;" />')
  
  return html
}

const showAuthorInfo = async () => {
  if (!article.value || !article.value.authorId) {
    return
  }
  try {
    const res = await getUserInfo(article.value.authorId)
    authorInfo.value = res.data
    authorDialogVisible.value = true
  } catch (error) {
    console.error('获取作者信息失败', error)
    ElMessage.error('获取作者信息失败')
  }
}

onMounted(() => {
  loadArticle()
  loadComments()
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

.el-main {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.article-card {
  margin-bottom: 20px;
}

.article-title {
  margin: 0 0 20px 0;
  font-size: 28px;
  color: #303133;
}

.article-meta {
  display: flex;
  gap: 20px;
  color: #909399;
  font-size: 14px;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
  align-items: center;
  flex-wrap: wrap;
}

.author-section {
  display: flex;
  align-items: center;
  gap: 10px;
}

.author-name {
  font-weight: bold;
  color: #409eff;
}

.author-info-dialog {
  text-align: center;
}

.author-avatar-section {
  margin-bottom: 20px;
}

.like-section {
  display: flex;
  align-items: center;
  gap: 5px;
}

.like-count {
  font-size: 14px;
  font-weight: bold;
}

.article-content {
  line-height: 1.8;
  color: #303133;
  font-size: 16px;
}

.comment-card {
  margin-top: 20px;
}

.comment-header {
  font-weight: bold;
  font-size: 16px;
}

.comment-input-section {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.comment-actions {
  margin-top: 10px;
  text-align: right;
}

.login-tip {
  text-align: center;
  padding: 20px 0;
  margin-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.comment-list {
  margin-top: 20px;
}

.comment-item {
  padding: 15px 0;
  border-bottom: 1px solid #f5f7fa;
}

.comment-user {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.username {
  font-weight: bold;
  color: #303133;
}

.comment-time {
  color: #909399;
  font-size: 12px;
}

.comment-content {
  color: #606266;
  line-height: 1.6;
  margin-bottom: 8px;
}

.comment-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
}

.likes {
  color: #909399;
  font-size: 14px;
}

@media (max-width: 768px) {
  .el-header {
    padding: 10px 15px;
  }

  .header-content h1 {
    font-size: 20px;
  }

  .el-main {
    padding: 15px;
  }

  .article-title {
    font-size: 22px;
  }

  .article-meta {
    gap: 10px;
    font-size: 12px;
  }

  .author-section {
    width: 100%;
  }

  .article-content {
    font-size: 15px;
  }

  .comment-user {
    gap: 8px;
  }
}

@media (max-width: 480px) {
  .header-content h1 {
    font-size: 18px;
  }

  .article-title {
    font-size: 20px;
  }

  .article-meta {
    gap: 8px;
    font-size: 11px;
  }

  .article-content {
    font-size: 14px;
  }

  .comment-footer {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
