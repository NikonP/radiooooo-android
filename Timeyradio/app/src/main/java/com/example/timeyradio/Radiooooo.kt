package com.example.timeyradio

import android.util.Log
import com.beust.klaxon.Klaxon

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.io.StringReader

class CodesByMoods(val data: HashMap<String, List<String>>)

class Radiooooo {
    private var _countryKey: String = "GBR"
    private var _yearKey: String = "1980"
    private var _moodKey: String = "FAST"

    private val _baseUrl: String = "https://radiooooo.app"
    private val _getSongEndpoint: String = "$_baseUrl/play"
    private val _getCodesEndpoint: String = "$_baseUrl/country/mood"

    private var _countryKeysList: List<String>? = null

    fun setConfig(countryKey: String, yearKey: String, moodKey: String) {
        _countryKey = countryKey
        _yearKey = yearKey
        _moodKey = moodKey
    }

    fun country(): String {
        return _countryKey
    }

    fun year(): String {
        return _yearKey
    }

    fun mood(): String {
        return _moodKey
    }

    fun getCodes(): List<String>? {
        return _countryKeysList
    }

    fun receiveCountryCodes() {
        val thread = Thread(Runnable {
            Log.d("debug", "!!!THREAD!!!")
            val (request, response, result) = _getCodesEndpoint
                .httpGet(listOf("decade" to _yearKey))
                .responseString()

            Log.d("debug", "Request: $request")

            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    Log.d("debug", "Request error: $ex")
                }
                is Result.Success -> {
                    val data = result.get()

                    if(data.length > 0) {
                        // так не очень хорошо делать, но я знаю что ответ будет в виде {string: string[], }
                        val moods = Klaxon().parseJsonObject(StringReader(data)) as MutableMap<String, List<String>>
                        val keys = moods.keys

                        var newCodes = mutableListOf<String>()
                        for((key, arr) in moods) {
                            for(code in arr) {
                                newCodes.add(code)
                            }
                        }

                        if(newCodes.size > 0) {
                            _countryKeysList = newCodes
                        } else {
                            Log.d("debug", "Warning. Empty codes");
                        }
                    } else {
                        Log.d("debug", "Warning. Empty response (in get codes)");
                    }
                }
            }
        })

        thread.start()
    }

    fun getNextSongUrl() {

    }
}