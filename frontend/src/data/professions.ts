import type { Profession, TradeChip } from '@/domain/models'

export const PROFESSIONS: Profession[] = [
  { id: 'software', title: 'Ingénierie logiciel', tagline: 'Apps, APIs, produits numériques', emoji: '💻', family: 'Numérique', imageName: 'profession_software' },
  { id: 'security', title: 'Sécurité informatique', tagline: 'SOC, pentest, gouvernance', emoji: '🛡️', family: 'Numérique', imageName: 'profession_security' },
  { id: 'data', title: 'Data & IA', tagline: 'Analytique, ML, tableaux de bord', emoji: '📊', family: 'Numérique', imageName: 'profession_software' },
  { id: 'design', title: 'Design & UX', tagline: 'Produit, interface, marque', emoji: '🎨', family: 'Numérique', imageName: 'profession_other' },
  { id: 'telecom', title: 'Télécoms', tagline: 'Réseaux, fibre, radio', emoji: '📡', family: 'Numérique', imageName: 'profession_security' },
  { id: 'management', title: 'Management', tagline: 'Pilotage d’équipes et de projets', emoji: '🧭', family: 'Business', imageName: 'profession_management' },
  { id: 'finance', title: 'Finance', tagline: 'Compta, audit, fintech', emoji: '💹', family: 'Business', imageName: 'profession_finance' },
  { id: 'hr', title: 'Ressources humaines', tagline: 'Recrutement et talents', emoji: '🤝', family: 'Business', imageName: 'profession_hr' },
  { id: 'sales', title: 'Commercial', tagline: 'Vente, grands comptes, retail', emoji: '🏷️', family: 'Business', imageName: 'profession_management' },
  { id: 'marketing', title: 'Marketing', tagline: 'Communication, digital, marque', emoji: '📣', family: 'Business', imageName: 'profession_other' },
  { id: 'electricity', title: 'Électricité', tagline: 'Bâtiments, industriels, SNEL', emoji: '⚡', family: 'Industrie', imageName: 'profession_other' },
  { id: 'energy', title: 'Énergie', tagline: 'Hydro, solaire, réseaux', emoji: '🔆', family: 'Industrie', imageName: 'profession_other' },
  { id: 'mechanics', title: 'Mécanique', tagline: 'Maintenance, atelier, engins', emoji: '🔧', family: 'Industrie', imageName: 'profession_other' },
  { id: 'civil', title: 'BTP & génie civil', tagline: 'Chantiers, routes, mines', emoji: '🏗️', family: 'Industrie', imageName: 'profession_other' },
  { id: 'mining', title: 'Mines', tagline: 'Géologie, process, HSE', emoji: '⛏️', family: 'Industrie', imageName: 'profession_other' },
  { id: 'logistics', title: 'Logistique', tagline: 'Supply, last-mile, entrepôts', emoji: '📦', family: 'Services', imageName: 'profession_other' },
  { id: 'transport', title: 'Transport', tagline: 'Flotte, aérien, transit', emoji: '🚚', family: 'Services', imageName: 'profession_other' },
  { id: 'health', title: 'Santé', tagline: 'Soins, pharma, coordination', emoji: '🩺', family: 'Services', imageName: 'profession_other' },
  { id: 'education', title: 'Éducation', tagline: 'Enseignement, formation', emoji: '📚', family: 'Services', imageName: 'profession_other' },
  { id: 'legal', title: 'Juridique', tagline: 'Droit des affaires, compliance', emoji: '⚖️', family: 'Services', imageName: 'profession_other' },
  { id: 'agriculture', title: 'Agriculture', tagline: 'Agribusiness, agronomie', emoji: '🌾', family: 'Services', imageName: 'profession_other' },
  { id: 'hospitality', title: 'Hôtellerie', tagline: 'Hotels, restauration, events', emoji: '🏨', family: 'Services', imageName: 'profession_other' },
  { id: 'plumbing', title: 'Plomberie', tagline: 'Sanitaire, réseaux, chantiers', emoji: '🚿', family: 'Industrie', imageName: 'profession_other' },
  { id: 'environment', title: 'Environnement', tagline: 'Climat, déchets, RSE', emoji: '🌿', family: 'Industrie', imageName: 'profession_other' },
  { id: 'admin', title: 'Administration publique', tagline: 'État, collectivités, ONG', emoji: '🏛️', family: 'Services', imageName: 'profession_other' },
  { id: 'journalism', title: 'Journalisme', tagline: 'Médias, radio, digital news', emoji: '🎙️', family: 'Services', imageName: 'profession_other' },
  { id: 'fashion', title: 'Mode & stylisme', tagline: 'Création, retail, ateliers', emoji: '👗', family: 'Services', imageName: 'profession_other' },
  { id: 'other', title: 'Autres', tagline: 'Tous les autres métiers', emoji: '✨', family: 'Services', imageName: 'profession_other' },
]

export const OTHER_PROFESSION = PROFESSIONS[PROFESSIONS.length - 1]!

export function professionById(id?: string | null): Profession {
  return PROFESSIONS.find((item) => item.id === id) ?? PROFESSIONS[0]!
}

export function professionFromCatalogName(name: string): Profession {
  const key = name
    .toLowerCase()
    .replaceAll('é', 'e')
    .replaceAll('è', 'e')
    .replaceAll('ê', 'e')
    .replaceAll('à', 'a')
    .replaceAll('ô', 'o')
  if (key.includes('cyber')) return professionById('security')
  if (key.includes('data') || key.includes('analyt')) return professionById('data')
  if (key.includes('fintech') || key.includes('digital')) return professionById('software')
  if (key.includes('develop') || key.includes('logiciel')) return professionById('software')
  if (key.includes('support') || key.includes('systeme')) return professionById('software')
  if (key.includes('telecom') || key.includes('reseau')) return professionById('telecom')
  if (key.includes('commercial')) return professionById('sales')
  if (key.includes('finance')) return professionById('finance')
  if (key.includes('management')) return professionById('management')
  if (key.includes('marketing')) return professionById('marketing')
  if (key.includes('humaine') || key.includes('rh')) return professionById('hr')
  if (key.includes('design') || key.includes('ux')) return professionById('design')
  return OTHER_PROFESSION
}

export function localTrades(): TradeChip[] {
  return PROFESSIONS.map((profession) => ({
    profession,
    title: profession.title,
    family: profession.family,
    tagline: profession.tagline,
    domainId: null,
  }))
}

export function inferProfession(title: string, skills: string[], searchAgent?: number | null): Profession {
  const text = `${title} ${skills.join(' ')}`.toLowerCase()
  const has = (...keys: string[]) => keys.some((key) => text.includes(key))
  if (has('cyber', 'sécurité info', 'securite', 'soc', 'pentest')) return professionById('security')
  if (has('data', 'analyste', 'machine learning', 'analytics')) return professionById('data')
  if (has('ux', 'ui/ux', 'graphiste', 'designer')) return professionById('design')
  if (has('télécom', 'telecom', 'fibre', 'réseau', 'reseau', 'wi-fi', 'wifi')) return professionById('telecom')
  if (has('dévelop', 'develop', 'software', 'program', 'android', 'kotlin', 'java', 'informatique', 'assistant it', 'systèmes it', 'navision')) {
    return professionById('software')
  }
  if (has('rh', 'recrut', 'talent', 'ressources humaines')) return professionById('hr')
  if (has('compta', 'audit', 'finance', 'trésor', 'tresor')) return professionById('finance')
  if (has('market', 'communication', 'marque')) return professionById('marketing')
  if (has('commercial', 'vente', 'business develop')) return professionById('sales')
  if (has('manager', 'chef de projet', 'pilotage', 'management', 'assistant(e) au projet', 'assistant au projet')) {
    return professionById('management')
  }
  if (has('électri', 'electri')) return professionById('electricity')
  if (has('mine', 'géolog', 'geolog')) return professionById('mining')
  if (has('santé', 'sante', 'médic', 'medic', 'pharma', 'infirm')) return professionById('health')
  if (has('logist', 'supply')) return professionById('logistics')
  if (has('transport', 'flotte')) return professionById('transport')
  if (has('enseignant', 'éducation', 'education', 'formation')) return professionById('education')
  if (has('jurid', 'avocat', 'compliance')) return professionById('legal')
  if (has('agri')) return professionById('agriculture')
  if (has('hôtel', 'hotel', 'restaur')) return professionById('hospitality')
  if (searchAgent === 1) return professionById('software')
  if (searchAgent === 2) return professionById('management')
  return OTHER_PROFESSION
}
