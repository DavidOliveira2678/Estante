<template>
  <div class="page-container">
    <div class="perfil-card">
      <div class="perfil-header">
        <div class="avatar-wrapper">
          <img :src="perfilEdit.avatar || defaultAvatar" alt="Avatar" class="avatar-img" />
        </div>
        <div class="perfil-title">
          <h2>Configurações da Conta</h2>
          <p>Gerencie seus dados e sua presença na Estante.</p>
        </div>
      </div>

      <form @submit.prevent="salvarAlteracoes" class="perfil-form">
        <div class="input-group">
          <label for="nome">Nome de Exibição</label>
          <input type="text" id="nome" v-model="perfilEdit.nome" required />
        </div>

        <div class="input-group">
          <label for="avatar">URL da Foto de Perfil</label>
          <input type="url" id="avatar" v-model="perfilEdit.avatar" placeholder="https://exemplo.com/sua-foto.jpg" />
          <small>Cole o link direto para uma imagem. Deixe em branco para usar o ícone padrão.</small>
        </div>

        <div class="input-group">
          <label for="bio">Biografia</label>
          <textarea id="bio" v-model="perfilEdit.bio" rows="4" placeholder="Fale um pouco sobre você e suas leituras favoritas..."></textarea>
        </div>

        <div class="actions">
          <button type="submit" class="btn-salvar">Salvar Alterações</button>
          <button type="button" class="btn-sair" @click="fazerLogout">Sair da Conta</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAutenticacaoStore } from '../stores/autenticacaoStore'
import defaultAvatar from '../assets/user_black.png'

const router = useRouter()
const autenticacaoStore = useAutenticacaoStore()
const perfilEdit = ref({ ...autenticacaoStore.usuario })
const salvarAlteracoes = () => {
  autenticacaoStore.atualizarPerfil(perfilEdit.value)
  alert('Perfil atualizado com sucesso!')
}
const fazerLogout = () => {
  autenticacaoStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.page-container {
  display: flex;
  justify-content: center;
  padding: 3rem 2rem;
  min-height: calc(100vh - 100px);
}
.perfil-card {
  background-color: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 2.5rem;
  width: 100%;
  max-width: 600px;
  box-shadow: var(--shadow-sm);
}
.perfil-header {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  margin-bottom: 2.5rem;
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 1.5rem;
}
.avatar-wrapper {
  width: 90px;
  height: 90px;
  border-radius: 50%;
  overflow: hidden;
  background-color: var(--bg-tertiary);
  border: 2px solid var(--brand-primary);
  display: flex;
  justify-content: center;
  align-items: center;
}
.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.perfil-title h2 {
  margin: 0 0 0.4rem 0;
  color: var(--text-primary);
}
.perfil-title p {
  margin: 0;
  color: var(--text-secondary);
  font-size: 0.95rem;
}
.perfil-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}
.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}
.input-group label {
  color: var(--text-primary);
  font-weight: 600;
  font-size: 0.95rem;
}
.input-group small {
  color: var(--text-muted);
  font-size: 0.8rem;
}
input, textarea {
  padding: 0.8rem;
  border-radius: 6px;
  border: 1px solid var(--border-color);
  background-color: var(--bg-primary);
  color: var(--text-primary);
  font-family: inherit;
  transition: border-color 0.2s;
}
input:focus, textarea:focus {
  outline: none;
  border-color: var(--brand-primary);
}
.actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1rem;
}
.btn-salvar {
  padding: 0.8rem 1.5rem;
  background-color: var(--brand-primary);
  color: #ffffff;
  font-weight: bold;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
}
.btn-salvar:hover {
  background-color: var(--brand-hover);
  transform: translateY(-2px);
}
.btn-sair {
  background: none;
  border: 1px solid #d9534f;
  color: #d9534f;
  padding: 0.7rem 1.2rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s ease;
}
.btn-sair:hover {
  background-color: #d9534f;
  color: #ffffff;
}
</style>