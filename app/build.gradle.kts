plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") // ✅ Added
}

android {
    namespace = "com.example.tutionmanagementappication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tutionmanagementappication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    // 🧩 UI + AndroidX
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // 🧪 Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // ✅ Firebase Dependencies
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation ("com.google.firebase:firebase-storage:20.3.0")



    // ✅ ZXing QR Scanner
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

}
