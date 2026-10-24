import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useSocialStore = defineStore('socialStore', () => {
  const friends = ref([
    { id: 1, name: 'Mácio', avatar: 'https://i.pravatar.cc/150?u=macio', booksRead: 42 },
    { id: 2, name: 'Pedro Gabriel', avatar: 'https://i.pravatar.cc/150?u=pedro', booksRead: 15 },
    { id: 3, name: 'Gabriel Veríssimo', avatar: 'https://i.pravatar.cc/150?u=gabriel', booksRead: 8 }
  ])
  const suggestedUsers = ref([
    { id: 4, name: 'Raí Miguel', avatar: 'https://i.pravatar.cc/150?u=rai', booksRead: 23 },
    { id: 5, name: 'Vitor José', avatar: 'https://i.pravatar.cc/150?u=vitor', booksRead: 31 }
  ])
  const feed = ref([
    { id: 1, userName: 'Mácio', userAvatar: 'https://i.pravatar.cc/150?u=macio', 
        action: 'escreveu uma resenha para', bookTitle: 'Clean Code', 
        bookCover: 'https://m.media-amazon.com/images/I/41xShlnTZTL._SY445_SX342_.jpg', rating: 5, 
        review: 'Leitura obrigatória...', date: 'Há 2 horas' }
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
  const addActivity = (activity) => {
    feed.value.unshift(activity)
  }
  return { friends, suggestedUsers, feed, addFriend, removeFriend, addActivity }
})