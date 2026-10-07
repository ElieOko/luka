const KEY = 'luka.buildSerial'

export function deviceSerial(): string {
  const existing = localStorage.getItem(KEY)
  if (existing) return existing
  const serial =
    typeof crypto !== 'undefined' && 'randomUUID' in crypto
      ? crypto.randomUUID()
      : `web-${Date.now()}-${Math.random().toString(36).slice(2)}`
  localStorage.setItem(KEY, serial)
  return serial
}
