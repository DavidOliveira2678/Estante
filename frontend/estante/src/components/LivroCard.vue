<template>
  <div class="livro-card" @click="goToDetails">
    <div class="capa-container">
      <!-- placeholder generico enquanto não tem backend -->
      <img :src="livro.capa || 'https://via.placeholder.com/150x220/2a2a2a/ffffff?text=Sem+Capa'" :alt="livro.titulo" class="capa-img" />
      <div class="hover-overlay">
        <button class="btn-detalhes">Ver Detalhes</button>
      </div>
    </div>
    
    <div class="info-container">
      <h3 class="livro-titulo" :title="livro.titulo">{{ livro.titulo }}</h3>
      <p class="livro-autor">{{ livro.autor }}</p>
      
      <StatusLeitura :status="livro.status" :progresso="livro.progresso" />
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import StatusLeitura from './StatusLeitura.vue'

const props = defineProps({
  livro: { type: Object, required: true }
})

const router = useRouter()

const goToDetails = () => {
  router.push(`/livro/${props.livro.id}`)
}
</script>

<style scoped>
.livro-card {
  display: flex;
  flex-direction: column;
  width: 160px;
  cursor: pointer;
  transition: transform 0.2s ease;
}
.livro-card:hover {
  transform: translateY(-5px);
}
.capa-container {
  position: relative;
  width: 100%;
  height: 240px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 10px rgba(0,0,0,0.3);
}
.capa-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.hover-overlay {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.2s ease;
}
.capa-container:hover .hover-overlay {
  opacity: 1;
}
.btn-detalhes {
  background: #3fc43f;
  color: #111;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
}
.info-container {
  margin-top: 12px;
}
.livro-titulo {
  font-size: 1rem;
  color: var(--text-color);
  margin: 0 0 4px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.livro-autor {
  font-size: 0.85rem;
  color: #aaa;
  margin: 0;
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
</style>