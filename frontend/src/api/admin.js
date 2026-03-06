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
