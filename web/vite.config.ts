import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

const spring = { target: 'http://localhost:8080' }

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // The Spring Boot server (server/ module). Proxied so the page and the socket share one origin.
    proxy: {
      '/play': { target: 'ws://localhost:8080', ws: true },
      // Google sign-in. Not the string shorthand: that sets changeOrigin, and Spring would then send
      // Google's redirect and its own to localhost:8080 instead of back through this page.
      '/api': spring,
      '/oauth2': spring,
      '/login': spring,
      '/logout': spring,
    },
  },
})
