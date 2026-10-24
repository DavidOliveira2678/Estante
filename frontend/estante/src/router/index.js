import { createRouter, createWebHistory } from 'vue-router'
import Home from '../layouts/Home.vue'
import Login from '../layouts/Login.vue'
import Catalogo from '../layouts/Catalogo.vue'

const routes = [
  { path: '/', component: Home },
  { path: '/login', component: Login },
  { path: '/catalogo', component: Catalogo }
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})