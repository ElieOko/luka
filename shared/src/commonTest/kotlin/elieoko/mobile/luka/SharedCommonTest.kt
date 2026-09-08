package elieoko.mobile.luka

import elieoko.mobile.luka.domain.model.AppDestination
import elieoko.mobile.luka.domain.model.AuthChannel
import elieoko.mobile.luka.domain.model.AuthIdentifier
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.domain.model.UserProfile
import elieoko.mobile.luka.domain.model.UserSession
import elieoko.mobile.luka.domain.usecase.FeedPolicy
import elieoko.mobile.luka.domain.usecase.OfferFilters
import elieoko.mobile.luka.domain.usecase.RequestOtpUseCase
import elieoko.mobile.luka.domain.usecase.ResolveDestinationUseCase
import elieoko.mobile.luka.domain.usecase.applyFilters
import elieoko.mobile.luka.data.remote.FakeCatalog
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class LukaDomainTest {
    @Test
    fun getPlatformHasJvmActual() {
        assertTrue(getPlatform().name.isNotBlank())
    }

    private val resolve = ResolveDestinationUseCase()

    @Test
    fun unauthenticatedGoesToWelcome() {
        assertEquals(AppDestination.Welcome, resolve(null))
    }

    @Test
    fun afterLoginProfessionIsRequired() {
        assertEquals(AppDestination.Profession, resolve(session()))
    }

    @Test
    fun afterProfessionLocationIsRequired() {
        assertEquals(
            AppDestination.Location,
            resolve(session(profession = Profession.SOFTWARE_ENGINEERING)),
        )
    }

    @Test
    fun analysisIsRequiredOnce() {
        assertEquals(
            AppDestination.Analysis,
            resolve(
                session(
                    profession = Profession.SOFTWARE_ENGINEERING,
                    country = "CD",
                    region = "kinshasa",
                ),
            ),
        )
    }

    @Test
    fun homeWhenAnalysisLaunched() {
        assertEquals(
            AppDestination.Home,
            resolve(
                session(
                    profession = Profession.SOFTWARE_ENGINEERING,
                    country = "CD",
                    region = "kinshasa",
                    analysis = true,
                ),
            ),
        )
    }

    @Test
    fun phoneAndEmailAreAccepted() {
        assertEquals(AuthChannel.PHONE, RequestOtpUseCase.parseIdentifier("+243810000000").channel)
        assertEquals(AuthChannel.EMAIL, RequestOtpUseCase.parseIdentifier("grace@luka.cd").channel)
        assertFails { RequestOtpUseCase.parseIdentifier("12") }
    }

    @Test
    fun offerFiltersByRegionCityAndProfession() {
        val offers = FakeCatalog.offers
        val kinshasa = offers.applyFilters(OfferFilters(regionId = "kinshasa"))
        assertTrue(kinshasa.isNotEmpty())
        assertTrue(kinshasa.all { it.regionId == "kinshasa" })
        val goma = offers.applyFilters(OfferFilters(city = "Goma"))
        assertTrue(goma.all { it.city.equals("Goma", true) })
        val elec = offers.applyFilters(OfferFilters(professionId = Profession.ELECTRICITY.id))
        assertEquals(1, elec.size)
        assertEquals("Électricien industriel", elec.first().title)
        assertEquals(5, offers.take(5).size)
    }

    @Test
    fun starterPlanLocksToOneProfession() {
        val profile = session(profession = Profession.FINANCE).profile
        val allowed = FeedPolicy.allowedProfessions(profile, FakeCatalog.plans.first { it.id == "starter" })
        assertEquals(setOf(Profession.FINANCE), allowed)
        val pro = FeedPolicy.allowedProfessions(profile, FakeCatalog.plans.first { it.id == "pro" })
        assertTrue(pro.containsAll(Profession.entries))
    }

    private fun session(
        profession: Profession? = null,
        country: String? = null,
        region: String? = null,
        analysis: Boolean = false,
    ) = UserSession(
        token = "tok",
        profile = UserProfile(
            id = "u1",
            displayName = "Grace",
            bio = "",
            photoUrl = "",
            identifier = AuthIdentifier(AuthChannel.PHONE, "+243810000000"),
            profession = profession,
            countryCode = country,
            regionId = region,
            planId = "starter",
            extraProfessionIds = emptyList(),
            analysisLaunched = analysis,
            welcomeSeen = true,
            visibleToRecruiters = false,
        ),
    )
}
