import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAutenticacaoStore = defineStore('autenticacaoStore', () => {
  //simulando que o usuario iniciou a sessão 
  const isAuthenticated = ref(true)
  const usuario = ref({
    id: 1,
    nome: 'Victor Soares',
    email: 'victor@estante.com',
    avatar: '',
    bio: 'Desenvolvedor de software e entusiasta de tecnologia e boas leituras.'
  })
  const login = (credentials) => {
    isAuthenticated.value = true
  }
  const logout = () => {
    isAuthenticated.value = false
  }
  const atualizarPerfil = (novosDados) => {
    usuario.value = { ...usuario.value, ...novosDados }
  }
  return { isAuthenticated, usuario, login, logout, atualizarPerfil }
})