/// <reference types="vite/client" />

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

interface ImportMetaEnv {
  readonly VITE_APP_TITLE: string
  readonly VITE_API_BASE_URL: string
  readonly VITE_PORT: number
  readonly VITE_USE_MOCK: boolean
  readonly VITE_DEBUG: boolean
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}