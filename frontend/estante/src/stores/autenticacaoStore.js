import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '../services/api'

const TOKEN_KEY = 'estante_token'

export const useAutenticacaoStore = defineStore('autenticacaoStore', () => {
  const token = ref(localStorage.getItem(TOKEN_KEY))
  const usuario = ref(null)
  const isAuthenticated = computed(() => !!token.value)

  const mapUsuario = (u) => ({
    id: u.id,
    username: u.username,
    nome: u.displayName,
    avatar: u.avatarUrl,
    bio: u.bio
  })

  const logout = () => {
    token.value = null
    usuario.value = null
    localStorage.removeItem(TOKEN_KEY)
  }

  const carregarUsuario = async () => {
    if (!token.value) return
    try {
      const { data } = await api.get('/api/me')
      usuario.value = mapUsuario(data)
    } catch (error) {
      logout()
    }
  }

  const login = async ({ email, password }) => {
    const { data } = await api.post('/api/auth/login', { login: email, password })
    token.value = data.token
    localStorage.setItem(TOKEN_KEY, data.token)
    await carregarUsuario()
  }

  const registrar = async ({ name, username, email, password }) => {
    await api.post('/api/auth/register', {
      username, email, password, displayName: name
    })
  }

  const atualizarPerfil = async ({ nome, avatar, bio }) => {
    const { data } = await api.put('/api/me', {
      displayName: nome, avatarUrl: avatar, bio
    })
    usuario.value = mapUsuario(data)
  }

  return { token, usuario, isAuthenticated, login, registrar, carregarUsuario, atualizarPerfil, logout }
})