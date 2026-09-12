package elieoko.mobile.luka

import elieoko.mobile.luka.core.memoized
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
import elieoko.mobile.luka.domain.usecase.withPolicy
import elieoko.mobile.luka.data.mapper.mergeInto
import elieoko.mobile.luka.data.mapper.requiresOtp
import elieoko.mobile.luka.data.mapper.toAuthTokens
import elieoko.mobile.luka.data.mapper.toCity
import elieoko.mobile.luka.data.mapper.toJobOffer
import elieoko.mobile.luka.data.remote.ApiException
import elieoko.mobile.luka.data.remote.FakeCatalog
import elieoko.mobile.luka.data.remote.dto.CongoCityDto
import elieoko.mobile.luka.data.remote.dto.StoredJobOfferDto
import elieoko.mobile.luka.domain.model.LukaPlans
import elieoko.mobile.luka.domain.model.OrientationPersona
import elieoko.mobile.luka.domain.usecase.LiveInsights
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
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
    fun phoneIsAcceptedAndEmailIsRejected() {
        assertEquals(AuthChannel.PHONE, RequestOtpUseCase.parseIdentifier("+243810000000").channel)
        assertFails { RequestOtpUseCase.parseIdentifier("grace@luka.cd") }
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
        val reset = OfferFilters()
        assertTrue(reset.isEmpty)
        assertTrue(!OfferFilters(city = "Goma").isEmpty)
    }

    @Test
    fun orientationInsightsMatchPersona() {
        val pupil = LiveInsights.forPersona(OrientationPersona.PUPIL, FakeCatalog.offers)
        assertTrue(pupil.isNotEmpty())
        assertTrue(pupil.all { it.persona == OrientationPersona.PUPIL })
        val employer = LiveInsights.forPersona(OrientationPersona.EMPLOYER, FakeCatalog.offers)
        assertTrue(employer.isNotEmpty())
        assertTrue(employer.all { it.persona == OrientationPersona.EMPLOYER })
        assertEquals(employer.first().label, LiveInsights.demandFrom(FakeCatalog.offers).first().profession.title)
    }

    @Test
    fun demandStatsComeFromLiveOffers() {
        val stats = LiveInsights.demandFrom(FakeCatalog.offers)
        assertTrue(stats.isNotEmpty())
        assertEquals(FakeCatalog.offers.size, stats.sumOf { it.openings })
        assertTrue(stats.first().openings >= stats.last().openings)
    }

    @Test
    fun starterPlanLocksToOneProfession() {
        val profile = session(profession = Profession.FINANCE).profile
        val allowed = FeedPolicy.allowedProfessions(profile, LukaPlans.byId("starter"))
        assertEquals(setOf(Profession.FINANCE), allowed)
        val pro = FeedPolicy.allowedProfessions(profile, LukaPlans.byId("professional"))
        assertTrue(pro.containsAll(Profession.entries))
    }

    @Test
    fun paidPlansAreStudentThreeAndProfessionalFive() {
        assertEquals(2, LukaPlans.paid.size)
        assertEquals("3 $", LukaPlans.student.priceLabel)
        assertEquals("5 $", LukaPlans.professional.priceLabel)
        assertEquals(1L, LukaPlans.student.apiId)
        assertEquals(2L, LukaPlans.professional.apiId)
        assertEquals(LukaPlans.student, LukaPlans.byApiId(1))
        assertEquals(LukaPlans.professional, LukaPlans.byApiId(2))
        assertEquals(LukaPlans.professional, LukaPlans.byId("pro"))
        assertEquals(LukaPlans.professional, LukaPlans.byId("elite"))
        assertEquals(LukaPlans.professional, LukaPlans.byId("plus"))
        val student = FeedPolicy.allowedProfessions(
            session(profession = Profession.FINANCE).profile,
            LukaPlans.student,
        )
        assertEquals(setOf(Profession.FINANCE), student)
    }

    @Test
    fun congoPhoneIsNormalized() {
        assertEquals("+243810000000", elieoko.mobile.luka.core.PhoneNumbers.normalize("0810000000"))
        assertEquals("+243810000000", elieoko.mobile.luka.core.PhoneNumbers.normalize("+243 81 000 0000"))
        assertEquals("+243810000000", elieoko.mobile.luka.core.PhoneNumbers.normalize("810000000"))
    }

    @Test
    fun catalogNamesMapToLocalProfessions() {
        assertEquals(Profession.SOFTWARE_ENGINEERING, Profession.fromCatalogName("Développement logiciel"))
        assertEquals(Profession.CYBER_SECURITY, Profession.fromCatalogName("Cybersécurité"))
        assertEquals(Profession.TELECOM, Profession.fromCatalogName("Télécom & réseaux"))
        assertEquals(Profession.HUMAN_RESOURCES, Profession.fromCatalogName("Ressources humaines"))
        assertEquals(Profession.SALES, Profession.fromCatalogName("Commercial"))
    }

    @Test
    fun cityProvinceBecomesRegionSlug() {
        val city = CongoCityDto(21, "Kinshasa", "Kinshasa", true).toCity()
        assertEquals("kinshasa", city.regionId)
        assertEquals("Kinshasa", city.name)
        val goma = CongoCityDto(10, "Goma", "Nord-Kivu", true).toCity()
        assertEquals("nord-kivu", goma.regionId)
    }

    @Test
    fun storedOfferMapsToJobOffer() {
        val dto = StoredJobOfferDto(
            id = 58,
            searchAgent = 1,
            title = "Assistant IT — position nationale",
            employer = "AVSI",
            city = "Kinshasa",
            province = null,
            opportunityType = "Emploi",
            contractType = "3 mois, renouvelables",
            skills = listOf("Support utilisateurs", "Maintenance informatique"),
            advertisementUrl = "https://www.avsi.org/en/work-with-us/jobs/V-154",
            applicationUrl = null,
            publicationDate = "2026-09-01",
        )
        val offer = dto.toJobOffer()
        assertEquals("58", offer.id)
        assertEquals("AVSI", offer.company)
        assertEquals("Kinshasa", offer.city)
        assertEquals("kinshasa", offer.regionId)
        assertEquals(dto.advertisementUrl, offer.applyUrl)
        assertEquals(Profession.SOFTWARE_ENGINEERING, offer.profession)
        assertTrue(offer.postedAtEpochMs > 0)
        assertTrue(offer.summary.contains("Support"))
    }

    @Test
    fun authPayloadReadsNestedTokens() {
        val json = kotlinx.serialization.json.Json.parseToJsonElement(
            """{"accessToken":"aaa","refreshToken":"bbb","profile":{"userId":3,"phone":"+243810000000","username":"Grace","email":null,"isPremium":false,"isCertified":false,"profileCompleted":false}}""",
        )
        val tokens = json.toAuthTokens()
        assertEquals("aaa", tokens.accessToken)
        assertEquals("bbb", tokens.refreshToken)
        assertEquals(3L, tokens.user.userId)
        assertEquals("+243810000000", tokens.user.phone)
    }

    @Test
    fun requiresOtpFalseReadsUserAndSkipsSms() {
        val json = kotlinx.serialization.json.Json.parseToJsonElement(
            """{"requiresOtp":false,"deviceRecognized":true,"loginFlow":"direct","otpBypassed":true,"accessToken":"aaa","refreshToken":"bbb","user":{"userId":9,"username":"Talent Luka","phone":"+243827824163","city":"Kinshasa","country":"CD","isPremium":false,"isCertified":true,"profileCompleted":false}}""",
        )
        assertFalse(json.requiresOtp())
        val tokens = json.toAuthTokens()
        assertEquals("aaa", tokens.accessToken)
        assertEquals(9L, tokens.user.userId)
        assertEquals("+243827824163", tokens.user.phone)
        assertEquals("Kinshasa", tokens.user.city)
        assertEquals("CD", tokens.user.country)
        assertTrue(tokens.user.isCertified)
        val merged = tokens.user.mergeInto(
            identifier = AuthIdentifier(AuthChannel.PHONE, "+243827824163"),
            existing = null,
        ).copy(welcomeSeen = true, analysisLaunched = true)
        assertEquals(AppDestination.Home, resolve(UserSession(token = "aaa", profile = merged)))
    }

    @Test
    fun requiresOtpTrueWhenFlagMissingAndNoToken() {
        val json = kotlinx.serialization.json.Json.parseToJsonElement(
            """{"message":"Code OTP envoyé par SMS.","data":{"otpStatus":"pending"}}""",
        )
        val data = json.jsonObject["data"]
        assertTrue(data.requiresOtp())
    }

    @Test
    fun apiErrorMessageIsReadFromBody() {
        val error = ApiException.fromBody(
            400,
            """{"message":"Code OTP invalide ou expiré.","detailMessage":"Code OTP invalide ou expiré."}""",
        )
        assertEquals("Code OTP invalide ou expiré.", error.message)
    }

    @Test
    fun buildSerialErrorIsNotAConnectivityMessage() {
        val error = ApiException.fromBody(
            400,
            """{"message":"Le build_serial est obligatoire.","detailMessage":"Le build_serial est obligatoire."}""",
        )
        assertFalse(error.message.orEmpty().contains("Impossible de joindre", ignoreCase = true))
        assertFalse(error.message.orEmpty().contains("build_serial", ignoreCase = true))
        assertTrue(error.message.orEmpty().contains("code", ignoreCase = true))
    }

    @Test
    fun registerPhonePayloadIncludesStudentFlag() {
        val encoded = kotlinx.serialization.json.Json.encodeToString(
            elieoko.mobile.luka.data.remote.dto.PhoneRegisterRequest.serializer(),
            elieoko.mobile.luka.data.remote.dto.PhoneRegisterRequest(phone = "+243810000000", isStudent = false),
        )
        assertTrue(encoded.contains("phone"))
        assertTrue(encoded.contains("isStudent"))
    }

    @Test
    fun authBodiesSendCamelCaseBuildSerial() {
        val json = kotlinx.serialization.json.Json { encodeDefaults = true }
        val register = json.encodeToString(
            elieoko.mobile.luka.data.remote.dto.PhoneRegisterRequest.serializer(),
            elieoko.mobile.luka.data.remote.dto.PhoneRegisterRequest(
                phone = "+243827824163",
                isStudent = false,
                buildSerial = "luka-and-test",
            ),
        )
        val login = json.encodeToString(
            elieoko.mobile.luka.data.remote.dto.IdentifiantRequest.serializer(),
            elieoko.mobile.luka.data.remote.dto.IdentifiantRequest(
                identifier = "+243827824163",
                buildSerial = "luka-and-test",
            ),
        )
        assertTrue(register.contains("\"buildSerial\":\"luka-and-test\""))
        assertTrue(register.contains("\"isStudent\":false"))
        assertFalse(register.contains("build_serial"))
        assertTrue(login.contains("\"buildSerial\":\"luka-and-test\""))
        assertTrue(login.contains("\"identifier\":\"+243827824163\""))
    }

    @Test
    fun serializationFailureIsNotAConnectivityMessage() {
        val message = elieoko.mobile.luka.data.remote.mapClientTransportError(
            IllegalArgumentException("Serializer for class 'JsonObject' is not found"),
        )
        assertEquals("Impossible d’envoyer la requête. Réessaie.", message)
    }

    @Test
    fun unresolvedHostIsAConnectivityMessage() {
        val message = elieoko.mobile.luka.data.remote.mapClientTransportError(
            Exception("Unable to resolve host server.casanayo.com"),
        )
        assertEquals("Vérifie ta connexion.", message)
    }

    @Test
    fun deviceSerialIsStableAndPrefixed() {
        val serial = elieoko.mobile.luka.core.createDeviceSerial().memoized()
        val first = serial.value()
        val second = serial.value()
        assertEquals(first, second)
        assertTrue(first.startsWith("luka-"))
        assertTrue(first.length > 8)
    }

    @Test
    fun feedPolicyKeepsRemoteDomainOffers() {
        val profile = session(profession = Profession.SOFTWARE_ENGINEERING).profile.copy(domainId = 1)
        val feed = elieoko.mobile.luka.domain.model.HomeFeed(
            offers = FakeCatalog.offers,
            ads = emptyList(),
            news = emptyList(),
            stats = emptyList(),
            orientation = emptyList(),
            professionals = emptyList(),
            topProfession = null,
        )
        val filtered = feed.withPolicy(profile, LukaPlans.byId("starter"))
        assertEquals(FakeCatalog.offers.size, filtered.offers.size)
    }

    @Test
    fun userDtoMergeMapsCityCertifiedAndPremium() {
        val dto = elieoko.mobile.luka.data.remote.dto.UserDto(
            userId = 9,
            username = "Patrick",
            phone = "+243810000001",
            city = "Goma",
            country = "CD",
            isPremium = true,
            isCertified = true,
            profileCompleted = true,
        )
        val merged = dto.mergeInto(
            identifier = AuthIdentifier(AuthChannel.PHONE, "+243810000001"),
            existing = session().profile,
        )
        assertEquals("Patrick", merged.displayName)
        assertEquals("Goma", merged.cityName)
        assertEquals("nord-kivu", merged.regionId)
        assertEquals("CD", merged.countryCode)
        assertTrue(merged.isCertified)
        assertTrue(merged.isPremium)
        assertTrue(merged.isPro)
        assertEquals(LukaPlans.PROFESSIONAL, merged.planId)
        assertTrue(merged.profileCompleted)
    }

    @Test
    fun updateProfileRequestKeepsSwaggerKeys() {
        val encoded = kotlinx.serialization.json.Json.encodeToString(
            elieoko.mobile.luka.data.remote.dto.UpdateProfileRequest.serializer(),
            elieoko.mobile.luka.data.remote.dto.UpdateProfileRequest(
                fullName = "Grace Mwamba",
                email = "grace@luka.cd",
                city = "Kinshasa",
                country = "CD",
            ),
        )
        assertTrue(encoded.contains("fullName"))
        assertTrue(encoded.contains("email"))
        assertTrue(encoded.contains("city"))
        assertTrue(encoded.contains("country"))
    }

    @Test
    fun congoMnoDetectsVodacomFor827824163() {
        assertEquals(elieoko.mobile.luka.core.CongoMno.Vodacom, elieoko.mobile.luka.core.CongoMno.detect("827824163"))
        assertEquals(elieoko.mobile.luka.core.CongoMno.Vodacom, elieoko.mobile.luka.core.CongoMno.detect("+243827824163"))
        assertEquals(elieoko.mobile.luka.core.CongoMno.Vodacom, elieoko.mobile.luka.core.CongoMno.detect("82"))
        assertEquals(elieoko.mobile.luka.core.CongoMno.Orange, elieoko.mobile.luka.core.CongoMno.detect("850000000"))
        assertEquals(elieoko.mobile.luka.core.CongoMno.Airtel, elieoko.mobile.luka.core.CongoMno.detect("970000000"))
        assertEquals(elieoko.mobile.luka.core.CongoMno.Africell, elieoko.mobile.luka.core.CongoMno.detect("910000000"))
        assertEquals("827824163", elieoko.mobile.luka.core.CongoMno.nationalDigits("+243 827 824 163"))
    }

    @Test
    fun paymentAmountFollowsTauxLocal() {
        val usd = elieoko.mobile.luka.domain.model.PaymentCurrency("USD", "Dollar US", 1.0)
        val cdf = elieoko.mobile.luka.domain.model.PaymentCurrency("CDF", "Franc congolais", 2250.0)
        assertEquals("3 $", usd.format(3.0))
        assertEquals("5 $", usd.format(5.0))
        assertEquals("6750 FC", cdf.format(3.0))
        assertEquals("11250 FC", cdf.format(5.0))
        assertEquals(6750.0, cdf.amountFor(LukaPlans.student.usdAmount))
        assertEquals(11250.0, cdf.amountFor(LukaPlans.professional.usdAmount))
    }

    @Test
    fun paymentInitRequestKeepsSwaggerKeys() {
        val encoded = kotlinx.serialization.json.Json.encodeToString(
            elieoko.mobile.luka.data.remote.dto.PaymentInitRequest.serializer(),
            elieoko.mobile.luka.data.remote.dto.PaymentInitRequest(
                abonnementId = 1,
                devise = "USD",
                phone = "+243827824163",
            ),
        )
        assertTrue(encoded.contains("\"abonnementId\":1"))
        assertTrue(encoded.contains("\"devise\":\"USD\""))
        assertTrue(encoded.contains("\"phone\":\"+243827824163\""))
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
