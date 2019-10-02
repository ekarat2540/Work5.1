package com.example.android.guesstheword.screens.game

import android.content.IntentSender
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    // The current _word
    private var _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word
    // The current _score
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    private val currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime
    val currentTimeString = Transformations.map(currentTime){time ->
        DateUtils.formateElapsedTime(time)
    }
    private val timer: CountDownTimer
    // The list of words - the front of the list is the next _word to guess
    private lateinit var wordList: MutableList<String>

    init {
        Log.i("GameViewModel", "GameViewModel created")
        _word.value = ""
        _score.value = 0
        timer = object : CountDownTimer(COUNDOWN_TIME, ONE_SECOND){
            override fun onFinish() {
                _currentTime.value = DONE
                onGameFinish()
            }
            override fun onTick(millisUntilFinished:Long){
                _currentTime.value = millisUntilFinished/ ONE_SECOND
            }

        }
        timer.start()
    }
    companion object {
        private const val DONE = 0L

        private const val ONE_SECOND = 1000L

        private const val COUNDOWN_TIME = 6000L
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
    fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }
    fun onSkip() {
        if (!wordList.isEmpty()) {
            _score.value = _score.value?.minus(1)
        }
        nextWord()
    }

    fun onCorrect() {
        if (!wordList.isEmpty()) {
            _score.value = _score.value?.plus(1)
        }
        nextWord()
    }
    fun nextWord() {
        if (!wordList.isEmpty()) {
            resetList()
        }else{

            //Select and remove a _word from the list
            _word.value = wordList.removeAt(0)
        }

    }

    fun onGameFinish() {
        _eventGameFinish.value = true
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }
}