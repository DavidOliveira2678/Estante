<template>
  <div class="auth-page-container">
    <div class="auth-card">
      <div class="auth-header">
        <h2>Comece sua estante</h2>
        <p>Acompanhe suas leituras e descubra novos títulos.</p>
      </div>

      <RegisterForm 
        @register="executeRegistration" 
        @switchMode="goToLogin" 
      />
      <p v-if="erro" class="erro">{{ erro }}</p>.
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAutenticacaoStore } from '../stores/autenticacaoStore'
import RegisterForm from '../components/RegisterForm.vue'

const router = useRouter()
const autenticacaoStore = useAutenticacaoStore()
const erro = ref('')
const executeRegistration = async (userData) => {
  erro.value = ''
  try {
    await autenticacaoStore.registrar({
      ...userData,
      username: userData.username.replace(/^@/, '')
    })
    router.push('/login')
  } catch (e) {
    const dados = e.response?.data
    erro.value = dados?.fields && Object.keys(dados.fields).length
      ? Object.values(dados.fields).join(' ')
      : dados?.message || 'Não foi possível criar a conta.'
  }
}
const goToLogin = () => router.push('/login')
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