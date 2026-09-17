import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // The Spring Boot server (server/ module). Proxied so the page and the socket share one origin.
    proxy: {
      '/play': { target: 'ws://localhost:8080', ws: true },
    },
  },
})
