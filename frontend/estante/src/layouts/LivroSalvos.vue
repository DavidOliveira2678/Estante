<template>
  <div class="page-container">
    <header class="estante-header">
      <h2>Minha Estante</h2>
      <nav class="status-tabs">
        <button 
          :class="{ active: abaAtiva === 'todos' }" 
          @click="abaAtiva = 'todos'">Todos</button>
        <button 
          :class="{ active: abaAtiva === 'lendo' }" 
          @click="abaAtiva = 'lendo'">Lendo</button>
        <button 
          :class="{ active: abaAtiva === 'quero_ler' }" 
          @click="abaAtiva = 'quero_ler'">Quero Ler</button>
        <button 
          :class="{ active: abaAtiva === 'lido' }" 
          @click="abaAtiva = 'lido'">Lidos</button>
      </nav>
    </header>

    <div class="livros-grid">
      <p v-if="livrosFiltrados.length === 0" class="empty-msg">
        Nenhum livro encontrado nesta categoria.
      </p>

      <LivroCard 
        v-else
        v-for="livro in livrosFiltrados" 
        :key="livro.id" 
        :livro="livro" 
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useLivroStore } from '../stores/livroStore'
import LivroCard from '../components/LivroCard.vue'

const livroStore = useLivroStore()
const abaAtiva = ref('todos')
const meusLivros = computed(() => {
  return livroStore.estante.map(item => {
    const livroCompleto = livroStore.getLivroById(item.livroId)
    return { ...livroCompleto, status: item.status }
  }).filter(livro => livro.titulo !== undefined)
})
const livrosFiltrados = computed(() => {
  if (abaAtiva.value === 'todos') {
    return meusLivros.value
  }
  return meusLivros.value.filter(livro => livro.status === abaAtiva.value)
})
</script>

<style scoped>
.page-container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}
.estante-header {
  margin-bottom: 2rem;
}
.estante-header h2 {
  color: var(--text-primary);
  margin: 0 0 1rem 0;
}
.status-tabs {
  display: flex; gap: 1rem;
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 1rem;
}
.status-tabs button {
  background: none; border: none;
  color: var(--text-secondary);
  font-size: 1rem; cursor: pointer; padding: 0.5rem 0;
}
.status-tabs button.active, .status-tabs button:hover {
  color: var(--brand-primary);
  font-weight: bold;
}
.livros-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 2rem;
}
.empty-msg {
  color: var(--text-muted);
  font-size: 1.1rem;
  grid-column: 1 / -1;
  margin-top: 2rem;
  }
</style>