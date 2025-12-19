package com.fp

import android.app.Application
import com.fp.data.AppContainer
import com.fp.data.DefaultAppContainer

/**
 * Application class for Product, responsible for initializing the
 * application-wide dependency injection container.
 */
class ProductApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}