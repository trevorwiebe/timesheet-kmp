package com.trevorwiebe.timesheet.di

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.initialize
import org.koin.dsl.module
import kotlin.experimental.ExperimentalNativeApi

actual fun createEmulatorConfig(): FirebaseEmulatorConfig = IosFirebaseEmulatorConfig()

class IosFirebaseEmulatorConfig : FirebaseEmulatorConfig {
    override fun setupEmulators(firebase: Firebase) {
        // Use localhost for iOS simulator
        val emulatorHost = "localhost"
        firebase.auth.useEmulator(emulatorHost, 9099)
        firebase.firestore.useEmulator(emulatorHost, 8080)
    }
}

@OptIn(ExperimentalNativeApi::class)
actual val platformModule = module {
    single {
        // Set debug flag for iOS
        Debug.isDebug = Platform.isDebugBinary
        Firebase.initialize()
    }
}