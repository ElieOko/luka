# Luka Web

Plateforme web Luka (Vue 3, TypeScript, Pinia, Axios, Vue Router).

Interface **desktop-first** : landing marketing, authentification en écran partagé, wizard métier / ville, puis tableau de bord avec barre latérale. Ce n’est pas un clone de l’app mobile.

Les **mêmes endpoints** que `LukaApi` côté Kotlin, y compris `GET` / `PUT /api/v1/auth/preferences`.

```bash
cd frontend
npm install
npm run dev
```

| Route | Rôle |
| --- | --- |
| `/` | Landing publique |
| `/auth` | Connexion / inscription SMS |
| `/setup/metier` · `/ville` · `/analyse` | Onboarding |
| `/app/*` | Espace connecté (offres, news, marché, compte) |
