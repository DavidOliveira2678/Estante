import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useBookStore = defineStore('bookStore', () => {
  const catalogo = ref([
    { id: 1, titulo: 'Clean Code', autor: 'Robert C. Martin', ano: 2008, paginas: 464, sinopse: 'Mesmo um código ruim pode funcionar...', capa: 'https://m.media-amazon.com/images/I/41xShlnTZTL._SY445_SX342_.jpg' },
    { id: 2, titulo: 'O Senhor dos Anéis', autor: 'J.R.R. Tolkien', ano: 1954, paginas: 1200, sinopse: 'Na Terra-média, o jovem Frodo...', capa: 'https://m.media-amazon.com/images/I/715e215DkSL._SY466_.jpg' },
    { id: 3, titulo: 'Neuromancer', autor: 'William Gibson', ano: 1984, paginas: 320, sinopse: 'Case era o melhor dos cowboys...', capa: 'https://m.media-amazon.com/images/I/91NbsU4Gj8L._SY466_.jpg' },
    { id: 4, titulo: 'Sistemas Operacionais Modernos', autor: 'Andrew S. Tanenbaum', ano: 2015, paginas: 864, sinopse: 'O livro definitivo sobre sistemas...', capa: 'https://m.media-amazon.com/images/I/71u9sJmYtFL._SY466_.jpg' },
    { id: 5, titulo: 'Design Patterns', autor: 'GoF', ano: 1994, paginas: 395, sinopse: 'Catálogo de soluções padronizadas...', capa: 'https://m.media-amazon.com/images/I/81gtKoapHFL._SY466_.jpg' }
  ])
  const estante = ref([
    { livroId: 1, status: 'lendo' },
    { livroId: 5, status: 'quero_ler' }
  ])
  const atualizarStatus = (livroId, novoStatus) => {
    const index = estante.value.findIndex(item => item.livroId === livroId)
    if (novoStatus === 'nenhum') {
      if (index !== -1) estante.value.splice(index, 1)
    } else {
      if (index !== -1) {
        estante.value[index].status = novoStatus
      } else {
        estante.value.push({ livroId, status: novoStatus })
      }
    }
  }
  const getLivroById = (id) => {
    return catalogo.value.find(livro => livro.id === id)
  }
  const getStatusLivro = (id) => {
    const item = estante.value.find(item => item.livroId === id)
    return item ? item.status : 'nenhum'
  }
  return { 
    catalogo, 
    estante, 
    atualizarStatus, 
    getLivroById, 
    getStatusLivro 
  }
})