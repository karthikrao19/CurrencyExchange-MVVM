import java.util.Properties


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("com.google.dagger.hilt.android")
    id ("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.krtk.currencyexchange"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.krtk.currencyexchange"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        //multiDexEnabled true // Enable multidex here


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas") // Change the path to your desired location
            }
        }



       /* javaCompileOptions {
            annotationProcessorOptions {
                arguments += ["room.schemaLocation":"$projectDir/schemas".toString(),"room.incremental":"true","room.expandProjection":"true"]
            }
        }*/



        vectorDrawables {
            useSupportLibrary = true
        }


       // buildConfigField("String", "TMDB_API_KEY", "\"b96fea102cf04451b74519758d79cfd4\"")
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField("String", "TMDB_API_KEY", "\"${properties.getProperty("TMDB_API_KEY")}\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            buildConfigField ("boolean", "PREPROD", "true")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.android.volley:volley:1.2.1")
    implementation("androidx.benchmark:benchmark-common:1.1.1")

    testImplementation ("io.mockk:mockk:1.12.4")
    testImplementation("junit:junit:4.13.2")

   // testImplementation ("org.mockito:mockito-core:5.1.1")
    //testImplementation ("com.squareup.okhttp3:mockwebserver:5.0.0-alpha.3")

    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation ("androidx.multidex:multidex:2.0.1")
    val composeBom = platform("androidx.compose:compose-bom:2023.06.01")
    implementation(composeBom)

    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")


    //navigation-compose
    val navVersion = "2.6.0"
    implementation ("androidx.navigation:navigation-compose:$navVersion")

    // hilt
    implementation ("com.google.dagger:hilt-android:2.44.2")
    kapt ("com.google.dagger:hilt-compiler:2.44.2")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Coil-kt - An image loading library for Android backed by Kotlin Coroutines.
  //  implementation("io.coil-kt:coil-compose:2.4.0")

    val retrofitVersion = "2.9.0"
    //Moshi
    implementation ("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    implementation ("com.squareup.moshi:moshi-kotlin:1.14.0")
    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    // okhttp
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.10.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    val lifecycleVersion = "2.6.1"
    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    // ViewModel utilities for Compose
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    // Lifecycle utilities for Compose
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
    // Saved state module for ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion")
    // Annotation processor
    implementation ("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")

    //Room Database
    implementation ("androidx.room:room-runtime:2.5.2")
    kapt ("androidx.room:room-compiler:2.5.2")
    implementation ("androidx.room:room-ktx:2.5.2")



}