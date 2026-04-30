# 🌿 AgriSuivi – Application Android de Gestion de Ferme Maraîchère

![Android](https://img.shields.io/badge/Platform-Android-green?logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue?logo=jetpackcompose)
![Supabase](https://img.shields.io/badge/Backend-Supabase-3ECF8E?logo=supabase)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange)

---

## Présentation du Projet

**AgriSuivi** est une application mobile Android native développée en **Kotlin**, destinée aux gestionnaires de fermes maraîchères péri-urbaines. Elle permet de numériser et d'automatiser le suivi des cycles de culture, de la date de semis jusqu'à la récolte.

>  L'application devient le **cerveau de la ferme** : elle prédit les récoltes, archive visuellement l'expérience du fermier et envoie des alertes de maturité.

###  Problème résolu

La gestion manuelle des cycles agricoles entraîne des pertes de production importantes dues à des prévisions imprécises. AgriSuivi résout ce problème en offrant un outil mobile intuitif, utilisable en plein soleil, avec un accès instantané à l'information.

---

##  Fonctionnalités Principales

###  CRUD Complet
| Opération | Description |
|-----------|-------------|
| **Créer** | Enregistrer un nouveau cycle : variété, parcelle, date de semis, durée de croissance, photo initiale |
| **Lire** | Afficher la liste des parcelles actives avec compte à rebours et barre de progression |
| **Mettre à jour** | Ajouter des photos de suivi, signaler des parasites, marquer comme récoltée |
| **Supprimer** | Retirer une culture en cas d'échec (intempéries, maladies) |

### Fonctionnalités Clés
- **Suivi photo** – Historique visuel de l'évolution de chaque culture
- **Compte à rebours automatique** – Calcul en temps réel des jours restants avant la récolte
- **Tableau de bord** – Résumé des parcelles prêtes à récolter cette semaine
- **Notifications de maturité** – Alertes automatiques via WorkManager (toutes les 24h)
- **Synchronisation cloud** – Données et photos synchronisées avec Supabase
- **Signalement parasites** – Badge d'alerte sur les photos de suivi

---

##  Captures d'Écran

> *(Ajouter vos captures d'écran ici après les avoir prises)*

| Tableau de Bord | Nouvelle Culture | Détail Parcelle |
|:-:|:-:|:-:|
| ![Dashboard](screenshots/dashboard.png) | ![Add](screenshots/add_culture.png) | ![Detail](screenshots/detail.png) |

---

##  Architecture Technique

L'application suit le pattern **MVVM (Model-View-ViewModel)** recommandé par Google :

```
┌─────────────────────────────────────────────────────┐
│                   UI Layer (Compose)                 │
│         DashboardScreen / DetailScreen               │
│         AddEditCultureScreen                         │
└──────────────────────┬──────────────────────────────┘
                       │ collectAsState() / StateFlow
┌──────────────────────▼──────────────────────────────┐
│              ViewModel Layer (Hilt)                  │
│    DashboardViewModel / DetailViewModel              │
│    AddEditViewModel                                  │
└──────────────────────┬──────────────────────────────┘
                       │ suspend functions / Flow
┌──────────────────────▼──────────────────────────────┐
│              Repository Layer                        │
│    CultureRepository (interface)                     │
│    CultureRepositoryImpl                             │
└──────────────────────┬──────────────────────────────┘
                       │ coroutines
┌──────────────────────▼──────────────────────────────┐
│              Data Layer (Supabase)                   │
│    SupabaseDataSource                                │
│    PostgREST + Storage                               │
└─────────────────────────────────────────────────────┘
```

###  Structure du Projet

```
app/src/main/java/com/agrisuivi/
├── AgriSuiviApp.kt                   # Application Hilt + WorkManager
├── MainActivity.kt                    # Point d'entrée
├── data/
│   ├── remote/
│   │   ├── Dtos.kt                   # DTOs Supabase + mappers
│   │   └── SupabaseDataSource.kt     # Accès PostgREST + Storage
│   └── repository/
│       ├── CultureRepository.kt       # Interface du dépôt
│       └── CultureRepositoryImpl.kt  # Implémentation Supabase
├── di/
│   └── AppModule.kt                  # Modules Hilt
├── domain/model/
│   ├── CycleCulture.kt               # Modèle métier + calculs java.time
│   └── PhotoSuivi.kt                 # Photo de suivi
├── ui/
│   ├── AgriNavGraph.kt               # Navigation Compose
│   ├── components/
│   │   └── CultureCard.kt            # Composants réutilisables
│   ├── screens/
│   │   ├── dashboard/                # Tableau de bord
│   │   ├── addedit/                  # Formulaire création/modification
│   │   └── detail/                   # Détail + historique photos
│   └── theme/
│       └── Theme.kt                  # Thème Terre & Nature MD3
└── workers/
    └── MaturityNotificationWorker.kt  # Notifications WorkManager
```

---

##  Stack Technique

| Technologie | Version | Rôle |
|-------------|---------|------|
| **Kotlin** | 2.0.21 | Langage principal |
| **Jetpack Compose** | BOM 2024.12.01 | UI déclarative 100% |
| **Material Design 3** | — | Design system |
| **Hilt** | 2.51.1 | Injection de dépendances (KSP) |
| **Supabase-kt** | 3.0.2 | Backend PostgreSQL + Storage |
| **Ktor** | 3.0.3 | Client HTTP pour Supabase |
| **Coil** | 2.7.0 | Chargement et cache d'images |
| **WorkManager** | 2.10.0 | Notifications périodiques |
| **Navigation Compose** | 2.8.5 | Navigation entre écrans |
| **java.time** | — | Calcul des dates (desugar activé) |
| **kotlinx.serialization** | 1.7.3 | Sérialisation JSON |
| **Gradle** | 8.7 | Build system |

---

##  Base de Données Supabase

### Tables PostgreSQL

**`cycles_culture`**
```sql
id                     UUID PRIMARY KEY
variete                TEXT NOT NULL
numero_parcelle        TEXT NOT NULL
date_semis             DATE NOT NULL
duree_croissance_jours INTEGER NOT NULL
photo_url              TEXT (nullable)
statut                 TEXT (EN_COURS | RECOLTEE | ECHEC)
notes                  TEXT
created_at             BIGINT
```

**`photos_suivi`**
```sql
id               UUID PRIMARY KEY
cycle_culture_id UUID REFERENCES cycles_culture(id)
photo_url        TEXT NOT NULL
description      TEXT
signale_parasite BOOLEAN
created_at       BIGINT
```

### Storage
- Bucket **`cultures`** (public) – stockage des photos de parcelles

---

##  Instructions pour Compiler l'Application

### Prérequis
- **Android Studio** Hedgehog ou plus récent
- **JDK 17** minimum
- **Compte Supabase** gratuit sur [supabase.com](https://supabase.com)
- Connexion Internet pour le téléchargement des dépendances

### Étape 1 – Cloner le projet
```bash
git clone https://github.com/VOTRE_USERNAME/AgriSuivi.git
cd AgriSuivi
```

### Étape 2 – Configurer Supabase
1. Créer un projet sur [supabase.com](https://supabase.com)
2. Exécuter le fichier `supabase_schema.sql` dans le SQL Editor
3. Créer un bucket Storage nommé **`cultures`** avec accès **public**

### Étape 3 – Configurer les clés API
Dans `app/build.gradle.kts`, remplacer :
```kotlin
buildConfigField("String", "SUPABASE_URL",
    "\"https://VOTRE_PROJET.supabase.co\"")
buildConfigField("String", "SUPABASE_ANON_KEY",
    "\"VOTRE_CLE_ANON_eyJ...\"")
```
>  Les clés se trouvent dans : Supabase → Settings → API → **Clés API héritage → anon public**

### Étape 4 – Synchroniser et lancer
```
1. File → Sync Project with Gradle Files
2. Run → Run 'app'  (ou Shift+F10)
```

###  Notes importantes
- Gradle **8.7** est requis (configuré dans `gradle-wrapper.properties`)
- Le `configuration-cache` est **désactivé** pour compatibilité avec AGP 8.5
- La mémoire JVM est configurée à **4GB** (`-Xmx4g`) dans `gradle.properties`

---

##  Design

Thème **"Terre & Nature"** basé sur Material Design 3 :

| Élément | Couleur | HEX |
|---------|---------|-----|
| Primary | Vert forêt | `#2D6A4F` |
| Secondary | Brun terreux | `#795548` |
| Tertiary | Jaune tournesol | `#F9A825` |
| Background | Parchemin blanc | `#F8F5EF` |

- Formes **organiques et arrondies** (RoundedCornerShape jusqu'à 32dp)
- Typographie lisible en extérieur (**Quicksand** / **Open Sans**)
- `LinearProgressIndicator` pour visualiser la progression de croissance

---

##  Auteur

**Tshibumb** – Projet académique de gestion de ferme maraîchère  
Développé dans le cadre du cours de développement Android natif.

---

*"Montrez que le numérique peut servir la terre."* 🌱
