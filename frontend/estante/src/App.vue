<template>
  <div class="app-wrapper">
    <Navbar />
    <main class="main-content">
      <router-view />
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { watch } from 'vue'
import Navbar from './components/Navbar.vue'
import Footer from './components/Footer.vue'
import { useAutenticacaoStore } from './stores/autenticacaoStore'
import { useLivroStore } from './stores/livroStore'

const autenticacaoStore = useAutenticacaoStore()
const livroStore = useLivroStore()

watch(() => autenticacaoStore.isAuthenticated, (logado) => {
  if (logado) livroStore.carregarEstante()
  else livroStore.limparEstante()
}, { immediate: true })
</script>
<style>
.app-wrapper {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.main-content {
  flex: 1;
}
</style>