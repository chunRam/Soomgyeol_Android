package com.example.meditation.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MeditationViewModel : ViewModel() {

    private var countDownTimer: CountDownTimer? = null

    private val _initialTime = MutableLiveData<Long>() // 총 명상 시간 (초)
    val initialTime: LiveData<Long> = _initialTime

    private val _remainingTime = MutableLiveData<Long>()
    val remainingTime: LiveData<Long> = _remainingTime

    private val _isRunning = MutableLiveData<Boolean>(false)
    val isRunning: LiveData<Boolean> = _isRunning

    private val _isFinished = MutableLiveData<Boolean>(false)
    val isFinished: LiveData<Boolean> = _isFinished

    fun setInitialTime(seconds: Long) {
        _initialTime.value = seconds
        _remainingTime.value = seconds
        _isFinished.value = false
    }

    fun startMeditation() {
        val timeLeft = _remainingTime.value ?: return
        _isRunning.value = true

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(timeLeft * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _remainingTime.value = millisUntilFinished / 1000
            }

            override fun onFinish() {
                _isRunning.value = false
                _remainingTime.value = 0
                _isFinished.value = true
            }
        }.start()
    }

    fun pauseMeditation() {
        countDownTimer?.cancel()
        _isRunning.value = false
    }

    override fun onCleared() {
        super.onCleared()
        countDownTimer?.cancel()
    }
}
