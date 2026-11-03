import { defineStore } from 'pinia'
import { ref } from 'vue'
import api from '../services/api'

const STATUS_PARA_API = {
  lendo: 'READING',
  quero_ler: 'WANT_TO_READ',
  lido: 'READ',
  abandonado: 'ABANDONED'
}
const STATUS_DA_API = Object.fromEntries(
  Object.entries(STATUS_PARA_API).map(([front, back]) => [back, front])
)

export const useLivroStore = defineStore('bookStore', () => {
  const catalogo = ref([])
  const livrosCache = ref({})
  const estante = ref([])

  const mapLivro = (b) => ({
    id: b.id,
    titulo: b.title,
    autor: (b.authors ?? []).map(a => a.name).join(', ') || 'Autor desconhecido',
    capa: b.coverUrl,
    sinopse: b.description,
    ano: b.publishedYear,
    paginas: b.pageCount
  })

  const guardar = (livro) => {
    livrosCache.value[livro.id] = livro
    return livro
  }

  const buscarLivros = async (query = '') => {
    try {
      const termo = query.trim() || 'literatura brasileira'
      const { data } = await api.get('/api/books', { params: { title: termo, size: 20 } })
      catalogo.value = data.content.map(b => guardar(mapLivro(b)))
    } catch (error) {
      console.error('Erro ao buscar livros do backend:', error)
    }
  }

  const carregarLivro = async (id) => {
    if (livrosCache.value[id]) return livrosCache.value[id]
    try {
      const { data } = await api.get(`/api/books/${id}`)
      return guardar(mapLivro(data))
    } catch (error) {
      console.error('Erro ao carregar livro:', error)
    }
  }

  const getLivroById = (id) => livrosCache.value[id]
  const getStatusLivro = (id) => {
    const item = estante.value.find(i => i.livroId === id)
    return item ? item.status : 'nenhum'
  }

  const aplicarLocal = (livroId, status) => {
    const index = estante.value.findIndex(i => i.livroId === livroId)
    if (status === 'nenhum') {
      if (index !== -1) estante.value.splice(index, 1)
    } else if (index !== -1) {
      estante.value[index].status = status
    } else {
      estante.value.push({ livroId, status })
    }
  }

  const carregarEstante = async () => {
    try {
      const { data } = await api.get('/api/library')
      estante.value = data.map(i => ({
        livroId: i.bookId,
        status: STATUS_DA_API[i.status]
      }))
      await Promise.all(data.map(i => carregarLivro(i.bookId)))
    } catch (error) {
      console.error('Erro ao carregar a estante:', error)
    }
  }

  const limparEstante = () => {
    estante.value = []
  }

  const atualizarStatus = async (livroId, novoStatus) => {
    const anterior = getStatusLivro(livroId)
    if (anterior === novoStatus) return

    aplicarLocal(livroId, novoStatus)
    try {
      if (novoStatus === 'nenhum') {
        await api.delete(`/api/library/${livroId}`)
      } else if (anterior === 'nenhum') {
        await api.post('/api/library', { bookId: livroId, status: STATUS_PARA_API[novoStatus] })
      } else {
        await api.put(`/api/library/${livroId}`, { status: STATUS_PARA_API[novoStatus] })
      }
    } catch (error) {
      aplicarLocal(livroId, anterior)
      console.error('Erro ao atualizar a estante:', error)
    }
  }

  return {
    catalogo, estante,
    buscarLivros, carregarLivro, getLivroById,
    getStatusLivro, atualizarStatus, carregarEstante, limparEstante
  }
})