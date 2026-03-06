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

export const likeComment = (id) => {
  return request({
    url: `/comment/${id}/like`,
    method: 'post'
  })
}

export const unlikeComment = (id) => {
  return request({
    url: `/comment/${id}/like`,
    method: 'delete'
  })
}

export const hasLikedComment = (id) => {
  return request({
    url: `/comment/${id}/has-liked`,
    method: 'get'
  })
}
