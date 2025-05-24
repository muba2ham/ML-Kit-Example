package com.example.ml_android_examples.ui.viewmodel

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val mediaPlayer: MediaPlayer?
): ViewModel() {

    private val _headerName = MutableStateFlow<String>("")
    val headerName: StateFlow<String> = _headerName

    private val _mlKitViewNumber = MutableStateFlow<Int>(1)
    val mlKitViewNumber: StateFlow<Int> = _mlKitViewNumber

    fun setHeaderName(v: String) {
        _headerName.value = v
    }

    fun setMLKitViewNumber(v: Int) {
        _mlKitViewNumber.value = v
    }

    fun playDing() {
        mediaPlayer?.let {
            if (it.isPlaying)
                it.seekTo(0)

            it.start()
        }
    }

    override fun onCleared() {
        mediaPlayer?.release()
    }

}