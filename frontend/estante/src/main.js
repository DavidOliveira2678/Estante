import { createApp } from 'vue'
import App from './App.vue'
import { router } from './router'

import './styles/variable.css' 
import './styles/global.css'

createApp(App).use(router).mount('#app')