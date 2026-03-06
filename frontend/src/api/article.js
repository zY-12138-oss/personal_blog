import request from '@/utils/request'

export const getArticleList = (params) => {
  return request({
    url: '/article/list',
    method: 'get',
    params
  })
}

export const searchArticles = (params) => {
  return request({
    url: '/article/search',
    method: 'get',
    params
  })
}

export const getHotArticles = (limit) => {
  return request({
    url: '/article/hot',
    method: 'get',
    params: { limit }
  })
}

export const getArticle = (id) => {
  return request({
    url: `/article/${id}`,
    method: 'get'
  })
}

export const createArticle = (data) => {
  return request({
    url: '/article',
    method: 'post',
    data
  })
}

export const updateArticle = (id, data) => {
  return request({
    url: `/article/${id}`,
    method: 'put',
    data
  })
}

export const deleteArticle = (id) => {
  return request({
    url: `/article/${id}`,
    method: 'delete'
  })
}

export const likeArticle = (id) => {
  return request({
    url: `/article/${id}/like`,
    method: 'post'
  })
}

export const unlikeArticle = (id) => {
  return request({
    url: `/article/${id}/like`,
    method: 'delete'
  })
}

export const hasLikedArticle = (id) => {
  return request({
    url: `/article/${id}/has-liked`,
    method: 'get'
  })
}
