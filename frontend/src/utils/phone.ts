export function normalizePhone(raw: string): string {
  const digits = raw.replace(/\D/g, '')
  let national = digits
  if (digits.startsWith('243') && digits.length >= 12) national = digits
  else if (digits.startsWith('0') && digits.length >= 10) national = `243${digits.slice(1)}`
  else if (digits.length >= 9 && digits.length <= 10 && (digits.startsWith('8') || digits.startsWith('9'))) {
    national = `243${digits}`
  }
  if (national.length < 12) {
    throw new Error('Numéro trop court. Exemple : +243 81 000 0000')
  }
  return `+${national}`
}

export function phoneForPayment(raw: string): string {
  const digits = raw.replace(/\D/g, '')
  const national = digits.startsWith('243') ? digits.slice(3) : digits.startsWith('0') ? digits.slice(1) : digits
  if (national.length !== 9) throw new Error('Indique un numéro congolais de 9 chiffres.')
  return `243${national}`
}
