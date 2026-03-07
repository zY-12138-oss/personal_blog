import request from '@/utils/request'

export const uploadFile = (file) => {
  console.log('=== 开始上传文件 ===')
  console.log('文件名:', file.name)
  
  const formData = new FormData()
  formData.append('file', file)
  
  return request({
    url: '/simple/upload',
    method: 'post',
    data: formData
  })
}
