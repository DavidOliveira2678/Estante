import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useReviewStore = defineStore('reviewStore', () => {
  const reviews = ref([
    { id: 1, livroId: 1, user: 'Maria Silva', avatar: 'https://i.pravatar.cc/150?u=maria', rating: 5, text: 'Um clássico indispensável!', date: '24/10/2026' },
    { id: 2, livroId: 2, user: 'João Souza', avatar: '', rating: 4, text: 'O universo é incrível, mas a leitura é densa.', date: '20/10/2026' }
  ])
  const addReview = (livroId, reviewData) => {
    reviews.value.unshift({
      id: Date.now(),
      livroId,
      ...reviewData
    })
  }
  const getReviewsByLivroId = (livroId) => {
    return computed(() => reviews.value.filter(r => r.livroId === livroId))
  }
  return { reviews, addReview, getReviewsByLivroId }
})