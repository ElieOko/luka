package elieoko.mobile.luka.data.mapper

import elieoko.mobile.luka.data.local.entity.AdEntity
import elieoko.mobile.luka.data.local.entity.DemandStatEntity
import elieoko.mobile.luka.data.local.entity.NewsEntity
import elieoko.mobile.luka.data.local.entity.OfferEntity
import elieoko.mobile.luka.data.local.entity.OrientationEntity
import elieoko.mobile.luka.data.local.entity.ProfessionalEntity
import elieoko.mobile.luka.domain.model.DemandStat
import elieoko.mobile.luka.domain.model.JobOffer
import elieoko.mobile.luka.domain.model.NewsItem
import elieoko.mobile.luka.domain.model.OrientationPath
import elieoko.mobile.luka.domain.model.PlatformAd
import elieoko.mobile.luka.domain.model.Profession
import elieoko.mobile.luka.domain.model.Professional

fun JobOffer.toEntity() = OfferEntity(
    id, title, company, companyLogoUrl, profession.id, regionId, city, contract, salary, summary, applyUrl, postedAtEpochMs, isRemote,
)

fun OfferEntity.toDomain() = JobOffer(
    id, title, company, companyLogoUrl, Profession.fromId(professionId), regionId, city, contract, salary, summary, applyUrl, postedAtEpochMs, isRemote,
)

fun PlatformAd.toEntity() = AdEntity(id, title, subtitle, cta, imageName, destination)
fun AdEntity.toDomain() = PlatformAd(id, title, subtitle, cta, imageName, destination)

fun NewsItem.toEntity() = NewsEntity(id, title, excerpt, source, imageName, publishedAtEpochMs, url)
fun NewsEntity.toDomain() = NewsItem(id, title, excerpt, source, imageName, publishedAtEpochMs, url)

fun Professional.toEntity() = ProfessionalEntity(
    id, name, title, profession.id, city, bio, photoName, rating, missions, available,
)

fun ProfessionalEntity.toDomain() = Professional(
    id, name, title, Profession.fromId(professionId), city, bio, photoName, rating, missions, available,
)

fun OrientationPath.toEntity() = OrientationEntity(
    id, title, domain, duration, level, why, nextStep, imageName, demandScore,
)

fun OrientationEntity.toDomain() = OrientationPath(
    id, title, domain, duration, level, why, nextStep, imageName, demandScore,
)

fun DemandStat.toEntity() = DemandStatEntity(profession.id, openings, sharePercent, trend)
fun DemandStatEntity.toDomain() = DemandStat(Profession.fromId(professionId), openings, sharePercent, trend)
