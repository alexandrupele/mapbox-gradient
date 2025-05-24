package com.pelworks.mapboxgradient.data

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pelworks.mapboxgradient.R
import com.pelworks.mapboxgradient.domain.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

suspend fun readSessionData(application: Application): List<Location> =
    withContext(Dispatchers.IO) {
        val context = application.applicationContext
        context.resources.openRawResource(R.raw.session_data).use { inputStream ->
            InputStreamReader(inputStream).use { reader ->
                val locationType = object : TypeToken<List<Location>>() {}.type
                Gson().fromJson(reader, locationType)
            }
        }
    }