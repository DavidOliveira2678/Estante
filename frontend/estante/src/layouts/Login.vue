<template>
  <div class="auth-page-container">
    <div class="auth-card">
      <div class="auth-header">
        <h2>Bem-vindo de volta</h2>
        <p>Acesse sua estante.</p>
      </div>

      <LoginForm 
        @login="executeLogin" 
        @switchMode="goToCadastro" 
      />
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import LoginForm from '../components/LoginForm.vue'

const router = useRouter()
const executeLogin = (credentials) => {
  console.log('Enviando para API de Login:', credentials)
  // se der sucesso: router.push('/home')
}
const goToCadastro = () => {
  router.push('/cadastro') 
}
</script>
<style scoped>
.auth-page-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 150px);
  padding: 2rem;
}
.auth-card {
  background: #3fc43f;
  padding: 2.5rem;
  border-radius: 12px;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 10px 30px rgba(63, 196, 63, 0.3);
}
.auth-header {
  text-align: center;
  margin-bottom: 2rem;
}
.auth-header h2 {
  margin: 0 0 0.5rem 0;
  color: #111111;
  font-weight: 800;
}
.auth-header p {
  font-size: 0.95rem;
  color: #222222;
  margin: 0;
  font-weight: 500;
}
</style>