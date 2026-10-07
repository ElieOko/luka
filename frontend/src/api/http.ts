import axios, { AxiosError, type AxiosRequestConfig } from 'axios'
import { deviceSerial } from '@/utils/device'
import { ApiException } from './types'

const baseURL = import.meta.env.VITE_API_BASE_URL ?? ''

export const http = axios.create({
  baseURL,
  timeout: 30_000,
  headers: { 'Content-Type': 'application/json' },
  paramsSerializer: { indexes: null },
})

let accessToken: string | null = null

export function setAccessToken(token: string | null) {
  accessToken = token
}

http.interceptors.request.use((config) => {
  const serial = deviceSerial()
  config.params = { ...(config.params ?? {}), buildSerial: serial }
  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`
  }
  return config
})

function mapTransportError(error: unknown): string {
  const blob = error instanceof Error ? `${error.name} ${error.message}` : String(error)
  const network = [
    'Network Error',
    'ERR_NETWORK',
    'ECONNABORTED',
    'timeout',
    'Failed to fetch',
  ].some((token) => blob.toLowerCase().includes(token.toLowerCase()))
  return network ? 'Vérifie ta connexion.' : 'Impossible d’envoyer la requête. Réessaie.'
}

function messageFromBody(status: number, data: unknown): string {
  if (data && typeof data === 'object') {
    const body = data as Record<string, unknown>
    const text = [body.message, body.detailMessage, body.error]
      .find((value) => typeof value === 'string' && value.trim())
    if (typeof text === 'string') return text
  }
  if (status >= 500) return 'Le serveur est indisponible. Réessaie.'
  if (status === 401) return 'Session expirée. Reconnecte-toi.'
  return 'Réponse inattendue du serveur.'
}

export async function send<T>(config: AxiosRequestConfig): Promise<T> {
  try {
    const response = await http.request<T>(config)
    return response.data
  } catch (error) {
    if (error instanceof AxiosError) {
      const status = error.response?.status ?? 0
      if (!error.response) throw new ApiException(mapTransportError(error), 0)
      throw new ApiException(messageFromBody(status, error.response.data), status)
    }
    throw new ApiException(mapTransportError(error), 0)
  }
}
