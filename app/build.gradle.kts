plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)  // ← ajouter cette ligne
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}
android {
    namespace = "com.agrisuivi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.agrisuivi"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Clés Supabase configurées pour le projet AgriSuivi
        buildConfigField("String", "SUPABASE_URL", "\"https://uemdlniegtbrazfqeivl.supabase.co\"")
        buildConfigField(
            "String",
            "SUPABASE_ANON_KEY",
            "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVlbWRsbmllZ3RicmF6ZnFlaXZsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzY5Mjk0MzEsImV4cCI6MjA5MjUwNTQzMX0.lx-bF0fELOGeCJFxDwjFH1XWyWKnuwbo8oE1bFGtguM\""
        )
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Nécessaire pour java.time sur API < 26
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    // NOTE: PAS de bloc composeOptions ici – géré par le plugin kotlin.compose depuis Kotlin 2.0
}

dependencies {
    // Desugaring – java.time sur Android < API 26
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)

    // Compose BOM – gère toutes les versions Compose de façon cohérente
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons)
    implementation(libs.androidx.navigation.compose)

    // Material Components – requis pour Theme.Material3.DayNight.NoActionBar dans themes.xml
    implementation(libs.material)

    // Hilt – utilise KSP (plus rapide et moderne que KAPT)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Coil – chargement d'images
    implementation(libs.coil.compose)

    // WorkManager + intégration Hilt
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.work)
    ksp(libs.hilt.work.compiler)

    // Supabase
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.storage)
    implementation(libs.ktor.client.android)

    debugImplementation(libs.androidx.ui.tooling)
    // Sérialisation Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}
