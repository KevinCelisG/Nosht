package com.korlabs.nosht.presentation.screens.users.general.timer

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korlabs.nosht.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor() : ViewModel() {

    private val _timeLeft = MutableStateFlow(120)
    val timeLeft: StateFlow<Int> = _timeLeft
    private var timer: CountDownTimer? = null

    fun startTimer() {
        _timeLeft.value = 120
        timer?.cancel()

        timer = object : CountDownTimer(120000, 1000) { // 300000ms = 1 minute, 1000ms = 1 second
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                viewModelScope.launch {
                    _timeLeft.value = 0
                }
            }
        }.start()
    }

    fun finishTimer() {
        Log.d(Util.TAG, "Finish timer")

        timer?.cancel()
        _timeLeft.value = 0
    }
}