import request from '@/utils/request'

export const getCategoryList = () => {
  return request({
    url: '/category/list',
    method: 'get'
  })
}

export const createCategory = (data) => {
  return request({
    url: '/category',
    method: 'post',
    data
  })
}
