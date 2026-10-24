<template>
  <div class="page-container">
    <header class="estante-header">
      <h2>Minha Estante</h2>
      <nav class="status-tabs">
        <button class="active">Todos</button>
        <button>Lendo</button>
        <button>Quero Ler</button>
        <button>Lidos</button>
      </nav>
    </header>

    <div class="livros-grid">
      <LivroCard 
        v-for="livro in mockMeusLivros" 
        :key="livro.id" 
        :livro="livro" 
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import LivroCard from '../components/LivroCard.vue'

const mockMeusLivros = ref([
  { id: 1, titulo: 'Clean Code', autor: 'Robert C. Martin', status: 'lendo', progresso: 45, capa: 'https://m.media-amazon.com/images/I/41xShlnTZTL._SY445_SX342_.jpg' },
  { id: 5, titulo: 'Design Patterns', autor: 'GoF', status: 'quero_ler', capa: 'https://m.media-amazon.com/images/I/81gtKoapHFL._SY466_.jpg' },
  { id: 3, titulo: 'Barba Ensopada de sangue', autor: 'Daniel Galera', status: 'nenhum', capa: 'https://m.media-amazon.com/images/I/51f7HBLjg5S._SY445_SX342_ML2_.jpg' },
])
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
  color: #fff;
  margin: 0 0 1rem 0;
}
.status-tabs {
  display: flex;
  gap: 1rem;
  border-bottom: 1px solid #333;
  padding-bottom: 1rem;
}
.status-tabs button {
  background: none;
  border: none;
  color: #888;
  font-size: 1rem;
  cursor: pointer;
  padding: 0.5rem 0;
}
.status-tabs button.active, .status-tabs button:hover {
  color: #3fc43f;
  font-weight: bold;
}
.livros-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
  gap: 2rem;
  }
</style>