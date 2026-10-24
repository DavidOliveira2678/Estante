import { createRouter, createWebHistory } from 'vue-router'
import Home from '../layouts/Home.vue'
import Login from '../layouts/Login.vue'
import Catalogo from '../layouts/Catalogo.vue'
import Cadastro from '../layouts/Cadastro.vue'

const routes = [
  { path: '/', component: Home },
  { path: '/login', component: Login },
  { path: '/catalogo', component: Catalogo },
  { path: '/cadastro', component: Cadastro }
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})