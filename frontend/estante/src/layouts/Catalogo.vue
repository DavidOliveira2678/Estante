<template>
  <div class="page-container">
    <header class="catalogo-header">
      <h2>Catálogo</h2>
      <div class="search-bar">
        <input type="text" placeholder="Buscar título, autor ou gênero..." v-model="searchQuery" />
      </div>
    </header>

    <div class="livros-grid">
      <LivroCard 
        v-for="livro in livrosDisponiveis" 
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
const searchQuery = ref('')
const livrosDisponiveis = computed(() => livroStore.catalogo)
</script>

<style scoped>
.page-container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}
.catalogo-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}
.catalogo-header h2 {
  color: var(--text-primary);
  margin: 0;
}
.search-bar input {
  padding: 0.6rem 1rem;
  border-radius: 20px;
  border: 1px solid var(--border-color);
  background-color: var(--bg-secondary);
  color: var(--text-primary);
  width: 300px;
}
.search-bar input::placeholder {
  color: var(--text-muted);
}
.search-bar input:focus {
  outline: none;
  border-color: var(--brand-primary);
}
.livros-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 2rem;
  }
</style>