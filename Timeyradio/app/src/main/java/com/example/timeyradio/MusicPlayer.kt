package com.example.timeyradio

import android.media.MediaPlayer
import android.util.Log

class MusicPlayer {
    private val _mediaPlayer: MediaPlayer = MediaPlayer()
    private var _currentSong = Song("", "", "", "", "", "")

    fun setSong(song: Song) {
        try {
            if (song.url.isNotEmpty()) {
                _mediaPlayer.setDataSource(song.url)
                _mediaPlayer.prepare()
            }
        } catch (th: Throwable) {
            Log.d("debug", "Music player error: $th")
        }
    }

    fun play() {
        try {
            if (!_mediaPlayer.isPlaying) {
                _mediaPlayer.start()
            }
        } catch (th: Throwable) {
            Log.d("debug", "Music player error: $th")
        }
    }

    fun pause() {
        _mediaPlayer.pause()
    }

    fun restart() {
        _mediaPlayer.seekTo(0)
    }

    fun loop(state: Boolean) {
        _mediaPlayer.isLooping = state
    }

    fun isPlaying(): Boolean {
        return _mediaPlayer.isPlaying
    }

    fun songArtist(): String {
        return _currentSong.artist
    }

    fun songTitle(): String {
        return _currentSong.title
    }

    fun songAlbum(): String {
        return _currentSong.album
    }

    fun songCoverUrl(): String {
        return _currentSong.imgUrl
    }
}