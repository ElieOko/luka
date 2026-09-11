package elieoko.mobile.luka.domain.repository

import elieoko.mobile.luka.domain.model.AuthIdentifier
import elieoko.mobile.luka.domain.model.OtpChallenge
import elieoko.mobile.luka.domain.model.UserSession
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    val session: Flow<UserSession?>
    suspend fun current(): UserSession?
    suspend fun requestOtp(identifier: AuthIdentifier): OtpChallenge
    suspend fun resendOtp(identifier: AuthIdentifier)
    suspend fun verifyOtp(identifier: AuthIdentifier, code: String): UserSession
    suspend fun markWelcomeSeen()
    suspend fun saveProfession(professionId: String, domainId: Long? = null)
    suspend fun saveLocation(countryCode: String, regionId: String, cityName: String? = null)
    suspend fun markAnalysisLaunched()
    suspend fun updateProfile(displayName: String, bio: String, email: String = "")
    suspend fun selectPlan(planId: String, extraProfessionIds: List<String>)
    suspend fun saveCv(fileName: String, mimeType: String)
    suspend fun resetDemo()
}
