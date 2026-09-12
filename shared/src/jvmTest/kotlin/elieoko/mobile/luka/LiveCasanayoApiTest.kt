package elieoko.mobile.luka

import elieoko.mobile.luka.core.AppConfig
import elieoko.mobile.luka.core.createHttpClient
import elieoko.mobile.luka.core.withDeviceSerial
import elieoko.mobile.luka.data.mapper.toCity
import elieoko.mobile.luka.data.mapper.toJobOffer
import elieoko.mobile.luka.data.remote.LukaApi
import elieoko.mobile.luka.data.remote.TokenStore
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertTrue

class LiveCasanayoApiTest {
    @Test
    fun publicCatalogAndOffersDecode() = runBlocking {
        val api = LukaApi(
            http = createHttpClient().withDeviceSerial(elieoko.mobile.luka.core.createDeviceSerial()),
            config = AppConfig(),
            tokenStore = TokenStore(),
            json = Json { ignoreUnknownKeys = true; isLenient = true },
            deviceSerial = elieoko.mobile.luka.core.createDeviceSerial(),
        )
        val domains = api.listDomains()
        assertTrue(domains.isNotEmpty(), "domaines publics vides")
        assertTrue(domains.any { it.professions.isNotEmpty() })
        val cities = api.listCities()
        assertTrue(cities.isNotEmpty(), "villes publiques vides")
        assertTrue(cities.first().toCity().name.isNotBlank())
        val page = api.listOffers(size = 5)
        assertTrue(page.content.isNotEmpty(), "offres vides")
        val offer = page.content.first().toJobOffer()
        assertTrue(offer.title.isNotBlank())
        assertTrue(offer.company.isNotBlank())
        assertTrue(offer.applyUrl.startsWith("http"))
        val plans = api.listAbonnements()
        assertTrue(plans.any { it.id == 1L && it.amountUsd == 3.0 }, "abonnement étudiant id=1")
        assertTrue(plans.any { it.id == 2L && it.amountUsd == 5.0 }, "abonnement professionnel id=2")
        val devises = api.listDevises()
        assertTrue(devises.any { it.code.equals("USD", true) && it.tauxLocal == 1.0 })
        assertTrue(devises.any { it.code.equals("CDF", true) && it.tauxLocal >= 1000.0 })
    }
}
