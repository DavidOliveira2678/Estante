import { createRouter, createWebHistory } from 'vue-router'
import Home from '../layouts/Home.vue'
import Login from '../layouts/Login.vue'
import Cadastro from '../layouts/Cadastro.vue'
import Catalogo from '../layouts/Catalogo.vue'
import LivrosSalvos from '../layouts/LivroSalvos.vue'
import LivroDetalhes from '../layouts/LivroDetalhes.vue'
import Social from '../layouts/Social.vue'
import Perfil from '../layouts/Perfil.vue'
import { useAutenticacaoStore } from '../stores/autenticacaoStore'

const routes = [
  { path: '/', component: Home },
  { path: '/login', component: Login },
  { path: '/cadastro', component: Cadastro },
  { path: '/catalogo', component: Catalogo },
  { path: '/estante', component: LivrosSalvos },
  { path: '/social', component: Social },
  { path: '/livro/:id', component: LivroDetalhes },
  { path: '/perfil', component: Perfil },
]

export const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const protegidas = ['/estante', '/perfil', '/social']
  const auth = useAutenticacaoStore()
  if (protegidas.includes(to.path) && !auth.isAuthenticated) return '/login'
})