<template>
  <select class="status-select" v-model="statusSelecionado">
    <option value="nenhum">Nenhum</option>
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
const statusSelecionado = computed({
  get: () => livroStore.getStatusLivro(props.livroId),
  set: (novoValor) => livroStore.atualizarStatus(props.livroId, novoValor)
})
</script>

<style scoped>
.status-select {
  width: 100%;
  padding: 0.6rem;
  background-color: var(--bg-tertiary);
  color: var(--text-primary);
  border: 1px solid var(--border-color);
  border-radius: 6px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: inherit;
}
.status-select:hover {
  background-color: var(--brand-surface);
  border-color: var(--brand-primary);
}
.status-select:focus {
  outline: none;
  border-color: var(--brand-primary);
  box-shadow: 0 0 0 2px var(--brand-surface);
}
</style>