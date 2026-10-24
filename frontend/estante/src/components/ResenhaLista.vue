<template>
  <div class="review-list">
    <h3>Avaliações da Comunidade</h3>
    
    <div v-if="reviews.length === 0">
      <p>Nenhuma resenha ainda. Seja o primeiro a avaliar!</p>
    </div>

    <div v-for="review in reviews" :key="review.id" class="review-card">
      <div class="review-header">
        <strong>{{ review.user }}</strong>
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
defineProps({
  reviews: {
    type: Array,
    required: true
  }
})
</script>

<style scoped>
.review-card {
  border: 1px solid #eee;
  padding: 15px;
  margin-bottom: 10px;
  border-radius: 8px;
}
.review-header {
display: flex;
justify-content: space-between;
}
.date {
    font-size: 0.8em;
    color: #888;
}
.review-rating span {
    color: #ccc;
}
.review-rating span.filled {
    color: #ffd700;
}
.review-text {
    margin-top: 10px;
    line-height: 1.5;
    }
</style>