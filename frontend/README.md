# Luka Web

Version web de Luka (Vue 3, TypeScript, Pinia, Axios). Elle reprend le langage visuel de l’app mobile — crème, rouge Luka, photos Kinshasa, wordmark, barre type TikTok — et **les mêmes endpoints** que `LukaApi` côté Kotlin.

## Stack

- Vue 3 + TypeScript + Vite
- Pinia (session persistée) + Vue Router
- Axios (`buildSerial` + Bearer, comme le client Ktor)
- Animations CSS / transitions Vue (carousel, radar, cartes, barres de tendances)

## API (même contrat que le mobile)

Préférences métier (domaines) :

- `GET /api/v1/auth/preferences`
- `PUT /api/v1/auth/preferences` body `{ "domainIds": number[] }`

Autres chemins alignés sur `shared/.../LukaApi.kt` : auth OTP, profil, catalogue villes/domaines, offres, abonnements, paiements.

En développement, Vite proxifie `/api` vers `https://server.casanayo.com`.

## Lancer

```bash
cd frontend
npm install
npm run dev
```

Build : `npm run build`

## Parcours

1. Welcome cinématique (Kinshasa)
2. Auth SMS + type de compte apprenant / professionnel
3. Métier unique → ville RDC → analyses infinies
4. Accueil, offres, news, tendances, orientation, profil, abonnement
