<template>
  <select 
    class="status-select" 
    :value="statusAtual" 
    @change="mudarStatus"
  >
    <option value="nenhum">Adicionar à estante...</option>
    <option value="lendo">Lendo</option>
    <option value="quero_ler">Quero Ler</option>
    <option value="lido">Lido</option>
  </select>
</template>

<script setup>
import { computed } from 'vue'
import { useLivroStore } from '../stores/livroStore'

const props = defineProps({
  livroId: {
    type: Number,
    required: true
  }
})
const livroStore = useLivroStore()
const statusAtual = computed(() => {
  if (!props.livroId) return 'nenhum'
  return livroStore.getStatusLivro(props.livroId)
})
const mudarStatus = (event) => {
  const novoStatus = event.target.value
  livroStore.atualizarStatus(props.livroId, novoStatus)
}
</script>

<style scoped>
.status-container {
  display: flex;
  flex-direction: column;
  gap: 4px;
  width: 100%;
  margin-top: 8px;
}
.status-badge {
  font-size: 0.75rem;
  font-weight: 700;
  padding: 4px 8px;
  border-radius: 4px;
  display: inline-block;
  text-align: center;
  text-transform: uppercase;
}
.badge-lendo {
    background: rgba(63, 196, 63, 0.2);
    color: #3fc43f;
}
.badge-quero {
    background: rgba(255, 193, 7, 0.2);
    color: #ffc107;
}
.badge-lido {
    background: rgba(33, 150, 243, 0.2);
    color: #2196f3;
}
.badge-abandonado {
    background: rgba(244, 67, 54, 0.2);
    color: #f44336;
}
.badge-hidden {
    display: none;
}
.progress-bar-bg {
  height: 4px;
  background: #333;
  border-radius: 2px;
  overflow: hidden;
}
.progress-bar-fill {
  height: 100%;
  background: #3fc43f;
  transition: width 0.3s ease;
}
</style>