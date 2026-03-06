import request from '@/utils/request'

export const getTagList = () => {
  return request({
    url: '/tag/list',
    method: 'get'
  })
}

export const createTag = (data) => {
  return request({
    url: '/tag',
    method: 'post',
    data
  })
}
