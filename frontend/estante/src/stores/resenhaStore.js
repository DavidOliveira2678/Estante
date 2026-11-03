import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '../services/api'

export const useReviewStore = defineStore('reviewStore', () => {
  const resenhasPorLivro = ref({})
  const usuarios = ref({})

  const buscarUsuario = async (id) => {
    if (usuarios.value[id]) return usuarios.value[id]
    try {
      const { data } = await api.get(`/api/users/${id}`)
      usuarios.value[id] = {
        nome: data.displayName || data.username,
        avatar: data.avatarUrl || ''
      }
    } catch (error) {
      usuarios.value[id] = { nome: 'Usuário', avatar: '' }
    }
    return usuarios.value[id]
  }

  const mapResenha = (r) => ({
    id: r.id,
    userId: r.userId,
    livroId: r.bookId,
    rating: Math.round(r.rating / 2),
    text: r.text,
    date: new Date(r.createdAt).toLocaleDateString('pt-BR'),
    user: usuarios.value[r.userId]?.nome ?? 'Usuário',
    avatar: usuarios.value[r.userId]?.avatar ?? ''
  })

  const carregarResenhas = async (livroId) => {
    try {
      const { data } = await api.get(`/api/books/${livroId}/reviews`, {
        params: { size: 20, sort: 'createdAt,desc' }
      })
      const ids = [...new Set(data.content.map(r => r.userId))]
      await Promise.all(ids.map(buscarUsuario))
      resenhasPorLivro.value[livroId] = data.content.map(mapResenha)
    } catch (error) {
      console.error('Erro ao carregar resenhas:', error)
    }
  }

  const publicarResenha = async (livroId, { rating, text }) => {
    const { data } = await api.post('/api/reviews', {
      bookId: livroId,
      rating: rating * 2,
      text,
      containsSpoilers: false
    })
    await buscarUsuario(data.userId)
    const lista = resenhasPorLivro.value[livroId] ?? []
    resenhasPorLivro.value[livroId] = [mapResenha(data), ...lista]
  }

  const getReviewsByLivroId = (livroId) => {
    return computed(() => resenhasPorLivro.value[livroId] ?? [])
  }

  return { resenhasPorLivro, carregarResenhas, publicarResenha, getReviewsByLivroId }
})