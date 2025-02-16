plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.roombasedattendance"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.roombasedattendance"
        minSdk = 24
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

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {
    // Core Libraries
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")

    // Firebase Core Libraries
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-storage:21.0.1")
    implementation("com.google.firebase:firebase-firestore:25.1.1")
    implementation("com.google.firebase:firebase-messaging:24.0.3")

    // ML Kit and Vision Libraries
    implementation("com.google.mlkit:face-detection:16.1.7") // Face detection
    implementation("com.google.mlkit:language-id:17.0.6") // Language identification
    // Removed duplicate firebase-ml-vision as it is part of ML Kit

    // Glide for Image Loading
    implementation("com.github.bumptech.glide:glide:4.12.0")

    // CameraX Libraries
    implementation("androidx.camera:camera-camera2:1.4.0")
    implementation("androidx.camera:camera-lifecycle:1.4.0")
    implementation("androidx.camera:camera-core:1.4.0")
    implementation("androidx.camera:camera-view:1.4.0")



    // UI Components
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Firebase ML Model Downloader
    implementation("com.google.firebase:firebase-ml-modeldownloader:25.0.1")

    // Firebase UI
    implementation("com.firebaseui:firebase-ui-database:8.0.0")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // TensorFlow Lite
    implementation("org.tensorflow:tensorflow-lite:2.4.0")

    // Activity and Fragment KTX
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.fragment:fragment-ktx:1.8.5")


    // Google Play Services (Location and Geofencing)
    implementation("com.google.android.gms:play-services-location:21.3.0")  // Location Services
    implementation("com.google.android.gms:play-services-geofencing:18.0.0") // Geofencing

    // Geofencing and Location Permissions
    implementation("androidx.core:core-ktx:1.15.0")  // KTX Extensions for Core Library (for permissions)
    implementation("com.google.android.gms:play-services-maps:19.0.0") // Maps SDK (if you need maps support)


    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

