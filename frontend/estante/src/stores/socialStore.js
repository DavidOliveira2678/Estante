import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '../services/api'
import { useAutenticacaoStore } from './autenticacaoStore'

export const useSocialStore = defineStore('socialStore', () => {
  const friends = ref([])
  const resultadosBusca = ref([])
  const feed = ref([])

  const mapUsuario = (u) => ({
    id: u.id,
    name: u.displayName || u.username,
    username: u.username,
    avatar: u.avatarUrl || ''
  })

  const carregarAmigos = async () => {
    const auth = useAutenticacaoStore()
    if (!auth.usuario) return
    try {
      const { data } = await api.get(`/api/users/${auth.usuario.id}/following`, {
        params: { size: 100 }
      })
      friends.value = await Promise.all(
        data.content.map(async (f) => {
          const { data: u } = await api.get(`/api/users/${f.followingId}`)
          return mapUsuario(u)
        })
      )
    } catch (error) {
      console.error('Erro ao carregar amigos:', error)
    }
  }

  const buscarUsuarios = async (q) => {
    const termo = q.trim()
    if (termo.length < 2) {
      resultadosBusca.value = []
      return
    }
    const auth = useAutenticacaoStore()
    const { data } = await api.get('/api/users', { params: { q: termo, size: 12 } })
    resultadosBusca.value = data.content
      .filter(u => u.id !== auth.usuario?.id)
      .map(mapUsuario)
  }

  const estaSeguindo = (id) => friends.value.some(f => f.id === id)
  const seguir = async (user) => {
    await api.post(`/api/users/${user.id}/follow`)
    if (!estaSeguindo(user.id)) friends.value.push(user)
  }

  const deixarDeSeguir = async (id) => {
    await api.delete(`/api/users/${id}/follow`)
    friends.value = friends.value.filter(f => f.id !== id)
  }

  const addActivity = (activity) => {
    feed.value.unshift(activity)
  }

  return {
    friends, resultadosBusca, feed,
    carregarAmigos, buscarUsuarios, estaSeguindo, seguir, deixarDeSeguir, addActivity
  }
})