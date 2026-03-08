import { jwtDecode } from 'jwt-decode'

export const isTokenExpired = (token) => {
  if (!token) return true
  
  try {
    const decoded = jwtDecode(token)
    const currentTime = Date.now() / 1000
    return decoded.exp < currentTime
  } catch (error) {
    return true
  }
}

export const getTokenExpiration = (token) => {
  if (!token) return null
  
  try {
    const decoded = jwtDecode(token)
    return new Date(decoded.exp * 1000)
  } catch (error) {
    return null
  }
}
