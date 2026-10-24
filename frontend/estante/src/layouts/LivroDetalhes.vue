<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useLivroStore } from '../stores/livroStore'
import StatusLeitura from '../components/StatusLeitura.vue'
import ResenhaForm from '../components/ResenhaForm.vue'
import ResenhaLista from '../components/ResenhaLista.vue'

const route = useRoute()
const livroStore = useLivroStore()
const idDaUrl = Number(route.params.id)
const livroAtual = computed(() => {
  const livroEncontrado = livroStore.getLivroById(idDaUrl)
  return livroEncontrado || {
    titulo: 'Livro não encontrado', 
    autor: 'Desconhecido', 
    sinopse: 'Não conseguimos localizar este livro no catálogo.', 
    capa: 'https://via.placeholder.com/150x220/2a2a2a/ffffff?text=Erro'
  }
})
const resenhasDoLivro = computed(() => [])
const adicionarResenha = (novaResenha) => {
  console.log("Resenha adicionada:", novaResenha)
}
</script>

<template>
  <div class="page-container">
    <div class="book-header">
      <div class="book-cover-col">
        <img :src="livroAtual.capa" :alt="livroAtual.titulo" class="detail-cover" />
        
        <div class="action-buttons">
          <StatusLeitura :livroId="livroAtual.id" />
        </div>
      </div>

      <div class="book-info-col">
        <h1>{{ livroAtual.titulo }}</h1>
        <h3 class="author">{{ livroAtual.autor }}</h3>
        
        <div class="meta-data">
          <span>Ano: {{ livroAtual.ano }}</span> &bull;
          <span>Páginas: {{ livroAtual.paginas }}</span>
        </div>

        <div class="synopsis">
          <h4>Sinopse</h4>
          <p>{{ livroAtual.sinopse }}</p>
        </div>
      </div>
    </div>

    <hr class="divider" />

    <div class="resenhas-section">
      <ResenhaForm @new-review="adicionarResenha" />
      <ResenhaLista :reviews="resenhasDoLivro" />
    </div>
  </div>
</template>

<style scoped>
.page-container {
  padding: 2rem;
  max-width: 1000px;
  margin: 0 auto;
}
.book-header {
  display: flex;
  gap: 3rem;
}
.book-cover-col {
  width: 220px;
  flex-shrink: 0;
}
.detail-cover {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.4);
  margin-bottom: 1rem;
}
.action-buttons {
  width: 100%;
}
.book-info-col {
  flex-grow: 1;
  color: var(--text-primary);
}
.book-info-col h1 {
  margin: 0;
  color: var(--text-primary);
  font-size: 2.5rem;
}
.author {
  color: var(--text-secondary);
  font-size: 1.2rem;
  margin: 0.5rem 0 1.5rem 0;
}
.synopsis h4 {
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}
.synopsis p {
  line-height: 1.6;
  color: var(--text-secondary);
}
.divider {
  margin: 3rem 0;
  border: 0;
  border-top: 1px solid var(--border-color, #333);
}
.resenhas-section {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}
</style>