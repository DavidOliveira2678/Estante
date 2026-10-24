<template>
  <header class="navbar">
    <div class="logo">
      <router-link to="/">
        <span class="logo-text">Estante</span>
      </router-link>
    </div>
    <nav class="nav-links">
      <router-link to="/">Início</router-link>
      <router-link to="/catalogo">Catálogo</router-link>
      <router-link to="/estante">Seus livros</router-link>
      <router-link to="/social">Amigos</router-link>
      <router-link to="/login">Conta</router-link>
    </nav>
    <button class="theme-toggle" @click="toggleTheme" title="Mudar tema">
      <img :src="isDarkMode ? sunIcon : moonIcon" alt="Ícone de Tema" class="theme-icon"/>
    </button>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import sunIcon from '../assets/sunIcon.png'
import moonIcon from '../assets/moonIcon.png'

const isDarkMode = ref(true)

const toggleTheme = () => {
  isDarkMode.value = !isDarkMode.value
  document.documentElement.setAttribute('data-theme', isDarkMode.value ? 'dark' : 'light')
}

onMounted(() => {
  document.documentElement.setAttribute('data-theme', isDarkMode.value ? 'dark' : 'light')
})
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem 4rem;
  background-color: var(--bg-primary);
  border-bottom: 1px solid var(--border-color);
}
.logo-text {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--brand-primary);
}
.nav-links {
  display: flex;
  gap: 2rem;
}
.nav-links a {
  color: var(--text-primary);
  font-size: 0.95rem;
  font-weight: 500;
  padding-bottom: 0.2rem;
}
.nav-links a:hover {
  color: var(--brand-hover);
}
.nav-links a.router-link-exact-active {
  color: var(--text-primary);
  border-bottom: 2px solid var(--brand-primary);
}
.theme-toggle {
  background-color: var(--bg-tertiary);
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: background-color 0.2s ease;
}
.theme-toggle:hover {
  background-color: var(--border-color);
}
.theme-icon {
  width: 20px;
  height: 20px;
}
</style>