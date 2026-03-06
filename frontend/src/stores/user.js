import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)
  const role = ref(localStorage.getItem('role') || '')

  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function setUserInfo(info) {
    userInfo.value = info
  }

  function setRole(newRole) {
    role.value = newRole
    localStorage.setItem('role', newRole)
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    role.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('role')
  }

  return {
    token,
    userInfo,
    role,
    setToken,
    setUserInfo,
    setRole,
    logout
  }
})
