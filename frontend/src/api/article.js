import request from '@/utils/request'

export const getArticleList = (params) => {
  return request({
    url: '/article/list',
    method: 'get',
    params
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
