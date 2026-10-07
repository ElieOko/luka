import type { City, Country } from '@/domain/models'

export const RDC: Country = {
  code: 'CD',
  name: 'République Démocratique du Congo',
  flag: '🇨🇩',
}

export const FALLBACK_CITIES: City[] = [
  { id: 'gombe', name: 'Gombe', regionId: 'kinshasa' },
  { id: 'limete', name: 'Limete', regionId: 'kinshasa' },
  { id: 'ngaliema', name: 'Ngaliema', regionId: 'kinshasa' },
  { id: 'kinshasa-centre', name: 'Kinshasa', regionId: 'kinshasa' },
  { id: 'lubumbashi', name: 'Lubumbashi', regionId: 'haut-katanga' },
  { id: 'likasi', name: 'Likasi', regionId: 'haut-katanga' },
  { id: 'goma', name: 'Goma', regionId: 'nord-kivu' },
  { id: 'butembo', name: 'Butembo', regionId: 'nord-kivu' },
  { id: 'bukavu', name: 'Bukavu', regionId: 'sud-kivu' },
  { id: 'kisangani', name: 'Kisangani', regionId: 'tshopo' },
  { id: 'matadi', name: 'Matadi', regionId: 'kongo-central' },
  { id: 'boma', name: 'Boma', regionId: 'kongo-central' },
  { id: 'kolwezi', name: 'Kolwezi', regionId: 'lualaba' },
  { id: 'mbuji-mayi', name: 'Mbuji-Mayi', regionId: 'kasai-oriental' },
  { id: 'kananga', name: 'Kananga', regionId: 'kasai-central' },
  { id: 'mbandaka', name: 'Mbandaka', regionId: 'equateur' },
]

const REGIONS = [
  'kinshasa',
  'haut-katanga',
  'nord-kivu',
  'sud-kivu',
  'tshopo',
  'kongo-central',
  'lualaba',
  'kasai-oriental',
  'kasai-central',
  'equateur',
]

export function slug(value: string): string {
  return value
    .toLowerCase()
    .replaceAll('é', 'e')
    .replaceAll('è', 'e')
    .replaceAll('ê', 'e')
    .replaceAll('à', 'a')
    .replaceAll('â', 'a')
    .replaceAll('ô', 'o')
    .replaceAll('î', 'i')
    .replaceAll('ï', 'i')
    .replaceAll('ç', 'c')
    .replaceAll("'", '')
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-|-$/g, '')
}

export function regionIdFor(city?: string | null, province?: string | null): string {
  if (province) {
    const fromProvince = slug(province)
    if (REGIONS.includes(fromProvince)) return fromProvince
    return fromProvince
  }
  const name = city?.split(',')[0]?.trim() ?? ''
  const match = FALLBACK_CITIES.find((item) => item.name.toLowerCase() === name.toLowerCase())
  if (match) return match.regionId
  return slug(name) || 'rdc'
}
