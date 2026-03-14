import request from '@/utils/request'

export const getStats = () => {
  return request({
    url: '/admin/stats',
    method: 'get'
  })
}

export const getUserList = (params) => {
  return request({
    url: '/admin/users',
    method: 'get',
    params
  })
}

export const deleteUser = (id) => {
  return request({
    url: `/admin/user/${id}`,
    method: 'delete'
  })
}

export const getArticleList = (params) => {
  return request({
    url: '/admin/articles',
    method: 'get',
    params
  })
}

export const deleteArticle = (id) => {
  return request({
    url: `/admin/article/${id}`,
    method: 'delete'
  })
}

export const getCommentList = (params) => {
  return request({
    url: '/admin/comments',
    method: 'get',
    params
  })
}

export const deleteComment = (id) => {
  return request({
    url: `/admin/comment/${id}`,
    method: 'delete'
  })
}
