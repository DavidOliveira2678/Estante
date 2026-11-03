import axios from 'axios'
import { useAutenticacaoStore } from '../stores/autenticacaoStore'

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  }
})

api.interceptors.request.use(config => {
  const autenticacaoStore = useAutenticacaoStore()
  if (autenticacaoStore.token) {
    config.headers.Authorization = `Bearer ${autenticacaoStore.token}`
  }
  return config
}, error => {
  return Promise.reject(error)
})

export default api