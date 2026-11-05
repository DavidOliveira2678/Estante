<template>
  <section class="hero-section">
    <h1 class="hero-title">Estante</h1>
    
    <p class="hero-subtitle">
      Uma plataforma open-source desenvolvida para registrar, avaliar e 
      compartilhar suas leituras de forma acessível.
    </p>
    <p class="hero-description">
      Conectamos seu hábito de leitura a um diário virtual.
    </p>

    <div class="hero-actions">
      <router-link to="/cadastro" class="btn btn-primary">Crie sua conta</router-link>
      <router-link to="/catalogo" class="btn btn-secondary">Explorar Livros</router-link>
    </div>
  </section>
</template>

<style scoped>
.hero-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 6rem 2rem;
  max-width: 800px;
  margin: 0 auto;
}
.hero-title {
  font-size: 4.5rem;
  font-weight: 800;
  color: var(--brand-primary);
  margin-bottom: 1.5rem;
  background: linear-gradient(90deg, var(--brand-primary), var(--brand-hover));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
.hero-subtitle {
  font-size: 1.25rem;
  color: var(--text-primary);
  line-height: 1.6;
  margin-bottom: 1rem;
}
.hero-description {
  font-size: 1rem;
  color: var(--text-secondary);
  line-height: 1.5;
  margin-bottom: 3rem;
  max-width: 650px;
}
.hero-actions {
  display: flex;
  gap: 1.5rem;
}
.btn {
  padding: 0.8rem 1.5rem;
  font-size: 1rem;
  font-weight: 600;
  border-radius: 6px;
  transition: all 0.2s ease;
}
.btn-primary {
  background-color: var(--brand-primary);
  color: #ffffff !important;
  box-shadow: 0 4px 14px rgba(40, 167, 69, 0.3);
}
.btn-primary:hover {
  background-color: var(--brand-hover);
  transform: translateY(-2px);
}
.btn-secondary {
  background-color: var(--bg-tertiary);
  color: var(--text-primary);
  border: 1px solid var(--border-color);
}
.btn-secondary:hover {
  background-color: var(--border-color);
}
</style>