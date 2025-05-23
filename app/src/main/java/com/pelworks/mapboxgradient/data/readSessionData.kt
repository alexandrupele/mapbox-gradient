package com.pelworks.mapboxgradient.data

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pelworks.mapboxgradient.R
import com.pelworks.mapboxgradient.domain.model.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

suspend fun readSessionData(application: Application) : List<Location> {
    return withContext(Dispatchers.IO) {
        val context = application.applicationContext
        val inputStream = context.resources.openRawResource(R.raw.session_data)
        val reader = InputStreamReader(inputStream)

        val locationType = object : TypeToken<List<Location>>() {}.type
        val parsedList: List<Location> = Gson().fromJson(reader, locationType)

        reader.close()
        parsedList
    }
}