plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // 직접 추가
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.meditation"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.meditation"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // ✅ Firebase BoM (버전 통합)
    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))

    // ✅ 사용할 Firebase 제품 (Auth + Analytics 등)
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")

    // ✅ 여기에 Firestore 추가만 해주면 됨
    implementation("com.google.firebase:firebase-firestore")

    // 프로필
    implementation("com.google.android.material:material:1.11.0")

}
