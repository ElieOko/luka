import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { lukaApi } from '@/api/lukaApi'
import { cityFromDto, offerFromDto, tradesFromDomains } from '@/api/mappers'
import { FALLBACK_CITIES } from '@/data/catalog'
import { PARTNER_ADS } from '@/data/learner'
import { localTrades } from '@/data/professions'
import type { City, JobOffer, TradeChip } from '@/domain/models'
import { demandFrom } from '@/utils/destination'
import { useSessionStore } from './session'

export interface OfferFilters {
  city: string | null
  tradeKey: string | null
  professionId: string | null
}

export const useCatalogStore = defineStore('catalog', () => {
  const cities = ref<City[]>(FALLBACK_CITIES)
  const trades = ref<TradeChip[]>(localTrades())
  const offers = ref<JobOffer[]>([])
  const query = ref('')
  const filters = ref<OfferFilters>({ city: null, tradeKey: null, professionId: null })
  const refreshing = ref(false)
  const error = ref<string | null>(null)

  const filteredOffers = computed(() => {
    let list = offers.value
    if (filters.value.city) list = list.filter((offer) => offer.city === filters.value.city)
    if (filters.value.professionId) {
      list = list.filter((offer) => offer.profession.id === filters.value.professionId)
    }
    const needle = query.value.trim().toLowerCase()
    if (needle) {
      list = list.filter(
        (offer) =>
          offer.title.toLowerCase().includes(needle) ||
          offer.company.toLowerCase().includes(needle) ||
          offer.city.toLowerCase().includes(needle),
      )
    }
    return list
  })

  const stats = computed(() => demandFrom(offers.value))
  const ads = computed(() => PARTNER_ADS)
  const filterActive = computed(
    () => Boolean(filters.value.city || filters.value.tradeKey || query.value.trim()),
  )

  async function loadPublicCatalog() {
    try {
      const [cityDtos, domainDtos] = await Promise.all([lukaApi.listCities(), lukaApi.listDomains()])
      const nextCities = cityDtos.filter((item) => item.isActive !== false).map(cityFromDto)
      const nextTrades = tradesFromDomains(domainDtos)
      cities.value = nextCities.length ? nextCities : FALLBACK_CITIES
      trades.value = nextTrades.length ? nextTrades : localTrades()
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Catalogue indisponible.'
      cities.value = FALLBACK_CITIES
      trades.value = localTrades()
    }
  }

  async function refreshOffers() {
    const session = useSessionStore()
    try {
      const page = await lukaApi.listOffers({
        domainIds: session.profile?.domainId ? [session.profile.domainId] : [],
        size: 50,
      })
      offers.value = page.content.map(offerFromDto)
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Offres indisponibles.'
    }
  }

  async function bootstrap() {
    refreshing.value = true
    await loadPublicCatalog()
    await refreshOffers()
    refreshing.value = false
  }

  function onQuery(value: string) {
    query.value = value
  }

  function onCity(name: string | null) {
    filters.value = { ...filters.value, city: name }
  }

  function onProfession(chip: TradeChip | null) {
    filters.value = chip
      ? { ...filters.value, professionId: chip.profession.id, tradeKey: `${chip.domainId ?? 'local'}::${chip.title}` }
      : { ...filters.value, professionId: null, tradeKey: null }
  }

  function resetFilters() {
    filters.value = { city: null, tradeKey: null, professionId: null }
    query.value = ''
  }

  return {
    cities,
    trades,
    offers,
    query,
    filters,
    refreshing,
    error,
    filteredOffers,
    stats,
    ads,
    filterActive,
    loadPublicCatalog,
    refreshOffers,
    bootstrap,
    onQuery,
    onCity,
    onProfession,
    resetFilters,
  }
})
