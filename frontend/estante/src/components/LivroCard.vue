<template>
  <div class="livro-card">
    <div class="capa-container" @click="goToDetails">
      <img :src="livro.capa || 'https://via.placeholder.com/150x220/2a2a2a/ffffff?text=Sem+Capa'" :alt="livro.titulo" class="capa-img" />
      <div class="hover-overlay">
        <button class="btn-detalhes">Ver Detalhes</button>
      </div>
    </div>
    
    <div class="info-container">
      <h3 class="livro-titulo" :title="livro.titulo" @click="goToDetails">{{ livro.titulo }}</h3>
      <p class="livro-autor">{{ livro.autor }}</p>
      
      <div class="status-wrapper">
        <StatusLeitura :livroId="livro.id" />
      </div>
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
  box-shadow: var(--shadow-sm);
  cursor: pointer;
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
  background: var(--brand-primary);
  color: #ffffff;
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
  color: var(--text-primary);
  margin: 0 0 4px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
}
.livro-autor {
  font-size: 0.85rem;
  color: var(--text-secondary);
  margin: 0;
}
.status-wrapper {
  margin-top: 8px;
}
</style>