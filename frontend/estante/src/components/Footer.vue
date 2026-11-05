<template>
  <footer class="footer">
    <p>© {{ new Date().getFullYear() }} Estante</p>
  </footer>
</template>
<style scoped>
.footer {
  text-align: center;
  padding: 2rem;
  background-color: var(--bg-primary);
  color: var(--text-muted);
  font-size: 0.9rem;
  border-top: 1px solid var(--border-color);
  margin-top: auto;
}
</style>