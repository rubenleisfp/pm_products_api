package com.fp.vm

import androidx.lifecycle.ViewModel
import com.fp.data.Datasource
import com.fp.model.Artist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Your name on 14/09/2025.
 */
class ArtistViewModel : ViewModel() {
    private val _artists = MutableStateFlow<List<Artist>>(emptyList())
    val artists: StateFlow<List<Artist>> get() = _artists

    fun loadArtist() {
        _artists.value = Datasource().loadArtist()
    }
}