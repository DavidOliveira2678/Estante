import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import { router } from './router'
import './styles/global.css'
import './styles/variable.css'
import { useAutenticacaoStore } from './stores/autenticacaoStore'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
useAutenticacaoStore().carregarUsuario()
app.use(router)
app.mount('#app')