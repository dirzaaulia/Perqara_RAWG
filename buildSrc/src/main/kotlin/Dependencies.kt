import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies  {
    object AndroidX {
        private const val coreKtx = "androidx.core:core-ktx:${Version.coreKtx}"

        val implementation = arrayListOf<String>().apply {
            add(coreKtx)
        }
    }

    object Chucker {
        private const val debug = "com.github.chuckerteam.chucker:library:${Version.chucker}"
        private const val release = "com.github.chuckerteam.chucker:library-no-op:${Version.chucker}"

        val debugImplementation = arrayListOf<String>().apply {
            add(debug)
        }

        val releaseImplementation = arrayListOf<String>().apply {
            add(release)
        }
    }

    object Coil {
        private const val coil = "io.coil-kt:coil:${Version.coil}"

        val implementation = arrayListOf<String>().apply {
            add(coil)
        }
    }

    object Hilt {
        private const val android = "com.google.dagger:hilt-android:${Version.hilt}"
        private const val compiler = "com.google.dagger:hilt-android-compiler:${Version.hilt}"
        private const val navigationFragment = "androidx.hilt:hilt-navigation-fragment:${Version.hiltNavigation}"

        val implementation = arrayListOf<String>().apply {
            add(android)
            add(navigationFragment)
        }

        val kapt = arrayListOf<String>().apply {
            add(compiler)
        }
    }

    object Insetter {
        private const val insetter = "dev.chrisbanes.insetter:insetter:${Version.insetter}"

        val implementation = arrayListOf<String>().apply {
            add(insetter)
        }
    }

    object Kotlin {
        private const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Version.kotlin}"

        val implementation = arrayListOf<String>().apply {
            add(stdlib)
        }
    }

    object Lifecycle {
        private const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.lifecycleKtx}"
        private const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.lifecycleKtx}"

        val implementation = arrayListOf<String>().apply {
            add(runtime)
            add(viewModelKtx)
        }
    }

    object Material {
        private const val material = "com.google.android.material:material:${Version.material}"

        val implementation = arrayListOf<String>().apply {
            add(material)
        }
    }

    object Navigation {
        private const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Version.navigationKtx}"
        private const val runtime = "androidx.navigation:navigation-runtime-ktx:${Version.navigationKtx}"
        private const val uiKtx = "androidx.navigation:navigation-ui-ktx:${Version.navigationKtx}"

        val implementation = arrayListOf<String>().apply {
            add(fragmentKtx)
            add(runtime)
            add(uiKtx)
        }
    }

    object Other {
        private const val timber = "com.jakewharton.timber:timber:5.0.1"

        val implementation = arrayListOf<String>().apply {
            add(timber)
        }
    }

    object Paging3 {
        private const val runtime = "androidx.paging:paging-runtime-ktx:${Version.paging3}"

        val implementation = arrayListOf<String>().apply {
            add(runtime)
        }
    }

    object Retrofit {
        private const val logger = "com.squareup.okhttp3:logging-interceptor:${Version.okHttpLogging}"
        private const val moshi = "com.squareup.moshi:moshi-kotlin:${Version.moshi}"
        private const val retrofit = "com.squareup.retrofit2:converter-moshi:${Version.retrofit}"

        val implementation = arrayListOf<String>().apply {
            add(logger)
            add(moshi)
            add(retrofit)
        }
    }
}

fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.debugImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("debugImplementation", dependency)
    }
}

fun DependencyHandler.releaseImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("releaseImplementation", dependency)
    }
}