package com.example.timeyradio

import android.util.Log
import com.beust.klaxon.Json
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import java.io.StringReader

class Radiooooo() {
    private var _countryKey: String = "GBR"
    private var _yearKey: String = "1980"
    private var _moodKey: String = "FAST"

    private val _baseUrl: String = "https://radiooooo.app"
    private val _assetsUrl: String = "https://asset.radiooooo.com"
    private val _getSongEndpoint: String = "$_baseUrl/play"
    private val _getCodesEndpoint: String = "$_baseUrl/country/mood"

    private var _currentSong: Song = Song("", "", "", "", "", "")

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

    fun getNextSongUrl(moodSetting: String, isocodesSetting: String, decadesSetting: String) {
        val thread = Thread(Runnable {
            Log.d("debug", "!!!THREAD!!!")
            var moodSettingU = moodSetting.toUpperCase()
            var isocodesSettingU = isocodesSetting.toUpperCase()
            var decadesSettingU = decadesSetting.toUpperCase()
            Log.d("debug", moodSettingU + isocodesSettingU + decadesSettingU)

            //default
//            val (request, response, result) = _getSongEndpoint
//                .httpPost(listOf(
//                    "moods" to setOf(_moodKey),
//                    "decades" to setOf(_yearKey),
//                    "isocodes" to setOf(_countryKey)
//                )).responseString()
            //from the settings
            val (request, response, result) = _getSongEndpoint
                .httpPost(listOf(
                    "moods" to setOf(moodSettingU),
                    "decades" to setOf(decadesSettingU),
                    "isocodes" to setOf(isocodesSettingU)
                )).responseString()

            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    Log.d("debug", "Request error: $ex")
                }
                is Result.Success -> {
                    val data = result.get()

                    Log.d("debug", "data: $data")

                    if(data.isNotEmpty()) {
                        val songInfo = Klaxon().parseJsonObject(StringReader(data))
                        val title = songInfo["title"] as String
                        val artist = songInfo["artist"] as String
                        val album = songInfo["album"] as String
                        val year = songInfo["year"] as String

                        val links = songInfo["links"] as JsonObject
                        val fileDirectUrl = links["ogg"] as String

                        val imgData = songInfo["image"] as JsonObject
                        val imgPath = imgData["path"] as String
                        val imgName = imgData["filename"] as String
                        val imgUrl = "$_assetsUrl/$imgPath" + "medium/$imgName"

                        val song = Song(artist, album, title, year, fileDirectUrl, imgUrl)

                        Log.d("debug", "song: $songInfo")

                        _currentSong = song
                    } else {
                        Log.d("debug", "Warning. Empty response (in get codes)");
                    }
                }
            }
        })

        thread.start()
    }

    fun getCurrentSong(): Song {
        return _currentSong
    }
}