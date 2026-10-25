<template>
  <div class="review-list">
    <h3>Avaliações da Comunidade</h3>
    
    <div v-if="reviews.length === 0" class="empty-state">
      <p>Nenhuma resenha ainda. Seja o primeiro a avaliar!</p>
    </div>

    <div v-for="review in reviews" :key="review.id" class="review-card">
      <div class="review-header">
        <div class="user-info">
          <img 
            :src="review.avatar || defaultAvatar" 
            alt="Avatar do usuário" 
            class="user-avatar" 
          />
          <strong>{{ review.user }}</strong>
        </div>
        <span class="date">{{ review.date }}</span>
      </div>
      
      <div class="review-rating">
        <span v-for="star in 5" :key="star" :class="{ filled: star <= review.rating }">
          ★
        </span>
      </div>
      
      <p class="review-text">{{ review.text }}</p>
    </div>
  </div>
</template>

<script setup>
import defaultAvatar from '../assets/user_black.png'

defineProps({
  reviews: {
    type: Array,
    required: true
  }
})
</script>

<style scoped>
h3 {
  color: var(--text-primary);
  margin-bottom: 1rem;
}
.empty-state {
  color: var(--text-muted);
  font-style: italic;
}
.review-card {
  background-color: var(--bg-secondary);
  border: 1px solid var(--border-color);
  padding: 1.5rem;
  margin-bottom: 15px;
  border-radius: 8px;
}
.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: var(--text-primary);
  margin-bottom: 0.8rem;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}
.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  background-color: #e9ecef; 
  padding: 2px;
  border: 1px solid var(--border-color);
}
.date {
  font-size: 0.85rem;
  color: var(--text-muted);
}
.review-rating span {
  color: var(--border-color);
  font-size: 1.2rem;
}
.review-rating span.filled {
  color: #ffd700;
}
.review-text {
  margin-top: 10px;
  line-height: 1.5;
  color: var(--text-secondary);
}
</style>