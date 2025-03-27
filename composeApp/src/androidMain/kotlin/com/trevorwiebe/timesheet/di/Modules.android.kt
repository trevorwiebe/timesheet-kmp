package com.trevorwiebe.timesheet.di

import com.trevorwiebe.timesheet.core.data.createHttpClient
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.initialize
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


actual fun createEmulatorConfig(): FirebaseEmulatorConfig = AndroidFirebaseEmulatorConfig()

class AndroidFirebaseEmulatorConfig : FirebaseEmulatorConfig {
    override fun setupEmulators(firebase: Firebase) {
        // Use 10.0.2.2 for Android emulator to connect to host machine
        val emulatorHost = "10.0.2.2"
        firebase.auth.useEmulator(emulatorHost, 9099)
        firebase.firestore.useEmulator(emulatorHost, 8080)
    }
}

actual val platformModule = module {
    single {
        // Set debug flag based on BuildConfig
        Firebase.initialize(androidContext())
    }
}

actual fun httpClient(): HttpClient {
    return createHttpClient(OkHttp.create())
}