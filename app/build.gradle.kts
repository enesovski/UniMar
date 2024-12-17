plugins {
    alias(libs.plugins.android.application)

    //Added by Enes to setup google firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.x_force.unimar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.x_force.unimar"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.messaging)


    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Added by Enes to setup google firebase
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.firebase:firebase-auth")
    implementation(libs.firebase.firestore)
    implementation (libs.glide)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

    implementation ("com.google.firebase:firebase-storage:21.0.1")
    //Added by Yavuz to use recyclerView
    implementation(libs.firebase.ui.firestore) // Check for the latest version
    implementation(libs.play.services.auth)
    implementation(libs.recyclerview)
    implementation(libs.cardview)


}