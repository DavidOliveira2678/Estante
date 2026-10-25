<template>
  <form @submit.prevent="onSubmit" class="auth-form">
    <div class="input-group">
      <label for="name">Nome completo</label>
      <input type="text" id="name" v-model="userData.name" required placeholder="Ex: Machado de Assis" />
    </div>
    <div class="input-group">
      <label for="username">Nome de usuário</label>
      <input type="text" id="username" v-model="userData.username" required placeholder="@seunick" />
    </div>
    <div class="input-group">
      <label for="reg-email">E-mail</label>
      <input type="email" id="reg-email" v-model="userData.email" required placeholder="seu@email.com" />
    </div>
    <div class="input-group">
      <label for="reg-password">Senha</label>
      <input type="password" id="reg-password" v-model="userData.password" required placeholder="Mínimo 8 caracteres" minlength="8" />
    </div>
    <button type="submit" class="btn-submit">Criar Conta</button>
    <p class="switch-mode">
      Já possui uma conta? 
      <a href="#" @click.prevent="$emit('switchMode')">Faça login</a>
    </p>
  </form>
</template>

<script setup>
import { reactive } from 'vue'
const emit = defineEmits(['switchMode', 'register'])
const userData = reactive({
  name: '',
  username: '',
  email: '',
  password: ''
})
const onSubmit = () => {
  emit('register', { ...userData })
}
</script>

<style scoped>
.auth-form { 
  display: flex; 
  flex-direction: column; 
  gap: 1.2rem; 
}
.input-group { 
  display: flex; 
  flex-direction: column; 
  gap: 0.4rem; 
}
.input-group label {
  color: #111111;
  font-size: 0.9rem;
  font-weight: 700;
}
input { 
  padding: 0.8rem; 
  border: 2px solid transparent; 
  border-radius: 6px; 
  background: #ffffff;
  color: #111111;
  font-family: inherit;
  transition: border-color 0.2s;
}
input:focus { 
  outline: none;
  border-color: #111111;
}
input::placeholder {
  color: #888888;
}
.btn-submit { 
  margin-top: 0.5rem;
  padding: 0.9rem; 
  background: #111111;
  color: #3fc43f;
  font-weight: 800; 
  font-size: 1rem;
  border: none; 
  border-radius: 6px; 
  cursor: pointer; 
  transition: all 0.2s ease; 
}
.btn-submit:hover { 
  background: #2a2a2a;
  color: #ffffff;
  transform: translateY(-2px);
}
.switch-mode { 
  text-align: center; 
  font-size: 0.9rem; 
  margin-top: 1rem; 
  color: #222222; 
  font-weight: 500;
}
.switch-mode a { 
  color: #111111; 
  text-decoration: underline; 
  font-weight: 800; 
}
.switch-mode a:hover {
  text-decoration: none;
}
</style>