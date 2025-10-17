plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

android {
    namespace = "org.righteffort.ryebread"
    compileSdk = 36

    defaultConfig {
        applicationId = "org.righteffort.ryebread"
        minSdk = 34
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)  // prob
    implementation(libs.androidx.appcompat)  // doubtful
    // implementation(libs.androidx.constraintlayout)  // doubtful
    implementation(platform(libs.androidx.compose.bom))  // yes
    implementation(libs.androidx.compose.ui)  // prob
    implementation(libs.androidx.compose.ui.graphics)  // prob
    implementation(libs.androidx.compose.ui.tooling.preview)  // prob
    implementation(libs.androidx.compose.material3)  // yes
    implementation(libs.androidx.navigation.compose)  // ???
    // implementation(libs.androidx.tv.foundation)   // doubtful
    // implementation(libs.androidx.tv.material)  // doubtful
    // ??? implementation(libs.androidx.lifecycle.runtime.ktx)  // ???
    // ??? implementation(libs.androidx.activity.compose)  // ???
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui) // ???
    androidTestImplementation(platform(libs.androidx.compose.bom)) // ???
    androidTestImplementation(libs.androidx.compose.ui.test.junit4) // ???
    debugImplementation(libs.androidx.compose.ui.tooling) // ???
    debugImplementation(libs.androidx.compose.ui.test.manifest) // ???
}
