import request from '@/utils/request'

export const register = (data) => {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

export const login = (data) => {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

export const getUserInfo = (id) => {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}

export const updateUserInfo = (id, data) => {
  return request({
    url: `/user/${id}`,
    method: 'put',
    data
  })
}
