plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.cocoapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cocoapp"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.gridlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.etebarian:meow-bottom-navigation:1.2.0")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.61")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")
}