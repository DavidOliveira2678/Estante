<template>
  <div class="review-form">
    <h3>Escrever uma Resenha</h3>
    
    <div class="stars">
      <img 
        v-for="star in 5" 
        :key="star" 
        :src="starIcon"
        alt="Estrela de avaliação"
        class="star-icon"
        :class="{ active: star <= (hoverRating || rating) }"
        @click="rating = star"
        @mouseover="hoverRating = star"
        @mouseleave="hoverRating = 0"
      />
    </div>

    <textarea 
      v-model="reviewText" 
      placeholder="O que você achou desta obra?" 
      rows="4"
    ></textarea>
    
    <button @click="submitReview" :disabled="!rating || !reviewText">
      Publicar Avaliação
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import starIcon from '../assets/starIcon.png'

const emit = defineEmits(['new-review'])
const rating = ref(0)
const hoverRating = ref(0)
const reviewText = ref('')
const submitReview = () => {
  emit('new-review', {
    id: Date.now(),
    user: 'Você',
    rating: rating.value,
    text: reviewText.value,
    date: new Date().toLocaleDateString()
  })
  rating.value = 0
  reviewText.value = ''
}
</script>

<style scoped>
.stars {
  display: flex;
  gap: 5px;
}
.star-icon {
  width: 28px;
  height: 28px;
  cursor: pointer;
  filter: grayscale(100%) opacity(40%);
  transition: filter 0.2s ease, transform 0.1s;
}
.star-icon:hover {
  transform: scale(1.1);
}
.star-icon.active {
  filter: grayscale(0%) opacity(100%);
}
textarea {
  width: 100%;
  margin-top: 15px;
  padding: 10px;
  border-radius: 6px;
}
button {
  margin-top: 10px;
  cursor: pointer;
}
button {
  margin-top: 15px;
  padding: 0.8rem 1.5rem;
  background-color: var(--brand-primary);
  color: #111;
  font-weight: bold;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
}
button:hover:not(:disabled) {
  background-color: var(--brand-hover);
  transform: translateY(-2px);
}
button:disabled {
  background-color: var(--bg-tertiary);
  color: var(--text-secondary);
  cursor: not-allowed;
  transform: none;
}
textarea {
  width: 100%;
  margin-top: 15px;
  padding: 12px;
  border-radius: 6px;
  border: 1px solid var(--border-color);
  background-color: var(--bg-primary);
  color: var(--text-primary);
  font-family: inherit;
  resize: vertical;
}
</style>