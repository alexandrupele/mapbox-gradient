package com.pelworks.mapboxgradient.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pelworks.mapboxgradient.data.readSessionData
import com.pelworks.mapboxgradient.presentation.mapper.toMapState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state

    init {
        loadSessionData()
    }

    private fun loadSessionData() {
        viewModelScope.launch {
            val locations = readSessionData(getApplication())
            val mapState = withContext(Dispatchers.Default) {
                locations.toMapState()
            }
            _state.update { it.copy(mapState = mapState) }
        }
    }
}