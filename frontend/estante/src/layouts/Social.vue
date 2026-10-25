<template>
  <div class="social-page">
    
    <nav class="social-tabs">
      <button 
        :class="{ active: activeTab === 'feed' }" 
        @click="activeTab = 'feed'"
      >
        Atividade Recente
      </button>
      <button 
        :class="{ active: activeTab === 'friends' }" 
        @click="activeTab = 'friends'"
      >
        Meus Amigos
      </button>
      <button 
        :class="{ active: activeTab === 'discover' }" 
        @click="activeTab = 'discover'"
      >
        Encontrar Usuários
      </button>
    </nav>

    <div v-if="activeTab === 'feed'" class="tab-content feed-section">
      <div v-if="activities.length === 0" class="empty-state">
        Nenhuma atividade recente. Adicione amigos para ver o que eles estão lendo!
      </div>
      
      <div v-for="activity in activities" :key="activity.id" class="feed-card">
        <div class="feed-header">
          <img :src="activity.userAvatar" alt="Avatar" class="avatar" />
          <p class="action-text">
            <strong>{{ activity.userName }}</strong> {{ activity.action }} <strong>{{ activity.bookTitle }}</strong>
          </p>
          <span class="date">{{ activity.date }}</span>
        </div>
        <div class="feed-body">
          <img :src="activity.bookCover" :alt="activity.bookTitle" class="book-cover" />
          
          <div class="review-content">
            <div v-if="activity.rating > 0" class="stars">
              <img 
                v-for="n in 5" :key="n"
                :src="starIcon"
                alt="Estrela"
                class="star-icon"
                :class="{ filled: n <= activity.rating }"
              />
            </div>
            <p v-if="activity.review" class="review-text">"{{ activity.review }}"</p>
          </div>
        </div>
      </div>
    </div>
    <div v-else-if="activeTab === 'friends'" class="tab-content friends-section">
      <div class="user-grid">
        <div v-for="friend in friends" :key="friend.id" class="user-card">
          <img :src="friend.avatar" alt="Avatar" class="avatar-large" />
          <h4>{{ friend.name }}</h4>
          <span class="stats">{{ friend.booksRead }} livros lidos</span>
          <button class="btn-remove" @click="removeFriend(friend.id)">Desfazer Amizade</button>
        </div>
      </div>
    </div>
    <div v-else-if="activeTab === 'discover'" class="tab-content discover-section">
      <div class="search-bar">
        <input type="text" placeholder="Buscar por nome de usuário..." />
        <button class="btn-search">Buscar</button>
      </div>

      <div class="user-grid">
        <div v-for="user in suggestedUsers" :key="user.id" class="user-card">
          <img :src="user.avatar" alt="Avatar" class="avatar-large" />
          <h4>{{ user.name }}</h4>
          <span class="stats">{{ user.booksRead }} livros lidos</span>
          <button class="btn-add" @click="addFriend(user)">Seguir</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import starIcon from '../assets/starIcon.png'

const activeTab = ref('feed')
const activities = ref([
  {
    id: 1,
    userName: 'David',
    userAvatar: 'https://lesifpe.com.br/assets/imagens/membros/david.jpeg',
    action: 'escreveu uma resenha para',
    bookTitle: 'Clean Code',
    bookCover: 'https://m.media-amazon.com/images/I/41xShlnTZTL._SY445_SX342_.jpg',
    rating: 5,
    review: 'Leitura obrigatória para qualquer desenvolvedor que queira escrever software sustentável. Mudou minha forma de programar!',
    date: 'Há 2 horas'
  },
  {
    id: 2,
    userName: 'Ingrid',
    userAvatar: 'https://lesifpe.com.br/assets/imagens/membros/ingrid.jpeg',
    action: 'adicionou à estante (Quero Ler)',
    bookTitle: 'Neuromancer',
    bookCover: 'https://m.media-amazon.com/images/I/91NbsU4Gj8L._SY466_.jpg',
    rating: 0,
    review: null,
    date: 'Há 5 horas'
  },
  {
    id: 3,
    userName: 'Montes',
    userAvatar: 'https://lesifpe.com.br/assets/imagens/membros/victormontes.jpeg',
    action: 'avaliou',
    bookTitle: 'O Senhor dos Anéis',
    bookCover: 'https://m.media-amazon.com/images/I/715e215DkSL._SY466_.jpg',
    rating: 5,
    review: null,
    date: 'Ontem'
  }
])
const friends = ref([
  { id: 1, name: 'David', avatar: 'https://lesifpe.com.br/assets/imagens/membros/david.jpeg', booksRead: 42 },
  { id: 2, name: 'Ingrid', avatar: 'https://lesifpe.com.br/assets/imagens/membros/ingrid.jpeg', booksRead: 15 },
  { id: 3, name: 'Montes', avatar: 'https://lesifpe.com.br/assets/imagens/membros/victormontes.jpeg', booksRead: 8 }
])
const suggestedUsers = ref([
  { id: 4, name: 'Raí', avatar: 'https://avatars.githubusercontent.com/u/202961793?s=130&v=4', booksRead: 23 },
  { id: 5, name: 'Arthur', avatar: 'https://avatars.githubusercontent.com/u/74386209?s=130&v=4', booksRead: 31 }
])
const removeFriend = (id) => {
  friends.value = friends.value.filter(f => f.id !== id)
}
const addFriend = (user) => {
  if (!friends.value.find(f => f.id === user.id)) {
    friends.value.push(user)
    suggestedUsers.value = suggestedUsers.value.filter(u => u.id !== user.id)
  }
}
</script>

<style scoped>
.social-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem 1rem;
}
.social-tabs {
  display: flex;
  gap: 1.5rem;
  border-bottom: 1px solid var(--border-color, #333);
  margin-bottom: 2rem;
}
.social-tabs button {
  background: none;
  border: none;
  color: var(--text-secondary, #888);
  font-size: 1.1rem;
  font-weight: bold;
  padding: 0.5rem 0;
  cursor: pointer;
  position: relative;
}
.social-tabs button:hover {
  color: var(--text-primary, #fff);
}
.social-tabs button.active {
  color: var(--brand-primary, #3fc43f);
}
.social-tabs button.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: var(--brand-primary, #3fc43f);
}
.feed-section {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}
.feed-card {
  background-color: var(--bg-secondary, #1e1e1e);
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.2);
}
.feed-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 1rem;
  border-bottom: 1px solid var(--border-color, #333);
  padding-bottom: 1rem;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}
.action-text {
  flex-grow: 1;
  color: var(--text-secondary, #bbb);
  font-size: 0.95rem;
  margin: 0;
}
.action-text strong {
  color: var(--text-primary, #fff);
}
.date {
  font-size: 0.8rem;
  color: #666;
}
.feed-body {
  display: flex;
  gap: 1.5rem;
}
.book-cover {
  width: 80px;
  border-radius: 4px;
  object-fit: cover;
}
.review-content {
  flex-grow: 1;
}
.stars {
  display: flex;
  gap: 2px;
  margin-bottom: 0.5rem;
}
.star-icon {
  width: 20px;
  height: 20px;
  filter: grayscale(100%) opacity(30%);
}
.star-icon.filled {
  filter: grayscale(0%) opacity(100%);
}
.review-text {
  color: var(--text-primary, #ddd);
  font-style: italic;
  line-height: 1.5;
  margin: 0;
}
.user-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 1.5rem;
}
.user-card, .feed-card {
  background-color: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}
.avatar-large {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}
.user-card h4 {
  margin: 0;
  color: var(--text-primary);
}
.stats {
  font-size: 0.85rem;
  color: var(--text-secondary, #888);
}
.btn-remove, .btn-add {
  margin-top: 10px;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  width: 100%;
}
.btn-remove {
  background-color: transparent;
  border: 1px solid #d9534f;
  color: #d9534f;
}
.btn-remove:hover {
  background-color: #d9534f;
  color: #fff;
}
.btn-add {
  background-color: var(--brand-primary, #3fc43f);
  color: #111;
}
.search-bar {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
}
.search-bar input {
  flex-grow: 1;
  padding: 0.8rem;
  border-radius: 4px;
  border: 1px solid var(--border-color);
  background-color: var(--bg-primary);
  color: var(--text-primary);
}
.btn-search {
  padding: 0 1.5rem;
  background-color: var(--brand-primary);
  color: #111;
  font-weight: bold;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}
.btn-search:hover {
  background-color: var(--brand-hover);
}
</style>