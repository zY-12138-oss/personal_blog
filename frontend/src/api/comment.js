import request from '@/utils/request'

export const getCommentsByArticle = (articleId) => {
  return request({
    url: `/comment/article/${articleId}`,
    method: 'get'
  })
}

export const createComment = (data) => {
  return request({
    url: '/comment',
    method: 'post',
    data
  })
}

export const deleteComment = (id) => {
  return request({
    url: `/comment/${id}`,
    method: 'delete'
  })
}
