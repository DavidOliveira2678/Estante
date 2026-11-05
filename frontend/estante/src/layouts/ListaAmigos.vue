<template>
  <div class="social-container">
    <h2>Meus Amigos</h2>
    
    <ul v-if="friends.length > 0">
      <li v-for="friend in friends" :key="friend.id">
        <img :src="friend.avatar" alt="Avatar" width="40" />
        <span>{{ friend.name }}</span>
        <button @click="removeFriend(friend.id)">Desfazer Amizade</button>
      </li>
    </ul>
    <p v-else>Você ainda não adicionou nenhum amigo.</p>

    <div class="add-friend">
      <h3>Encontrar Usuários</h3>
      <ul>
        <li v-for="user in suggestedUsers" :key="user.id">
          {{ user.name }}
          <button @click="addFriend(user)">Adicionar</button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const friends = ref([
  { id: 1, name: 'Maria Silva', avatar: 'https://i.pravatar.cc/150?u=maria' }
])
const suggestedUsers = ref([
  { id: 2, name: 'João Souza', avatar: 'https://i.pravatar.cc/150?u=joao' },
  { id: 3, name: 'Ana Costa', avatar: 'https://i.pravatar.cc/150?u=ana' }
])
const addFriend = (user) => {
  if (!friends.value.find(f => f.id === user.id)) {
    friends.value.push(user)
    
    suggestedUsers.value = suggestedUsers.value.filter(u => u.id !== user.id)
  }
}

const removeFriend = (id) => {
  friends.value = friends.value.filter(f => f.id !== id)
}
</script>

<style scoped>
.social-container {
    max-width: 600px;
    margin: 0 auto;
}
li {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
    }
</style>