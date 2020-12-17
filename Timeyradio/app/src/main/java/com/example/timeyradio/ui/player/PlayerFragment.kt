package com.example.timeyradio.ui.player

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.timeyradio.MusicPlayer
import com.example.timeyradio.R
import com.example.timeyradio.Radiooooo
import kotlinx.android.synthetic.main.fragment_player.view.*

//выф
class PlayerFragment : Fragment() {

    private lateinit var playerViewModel: PlayerViewModel
    private val musicPlayer: MusicPlayer = MusicPlayer()
    private val radio: Radiooooo = Radiooooo(musicPlayer)
    private lateinit var prefs: SharedPreferences
    val countryKey = "COUNTRY_KEY"
    val yearKey = "YEAR_KEY"
    val moodKey = "MOOD_KEY"
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        playerViewModel =
                ViewModelProvider(this).get(PlayerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_player, container, false)
        prefs = (this.activity as Activity).getSharedPreferences("settings", Context.MODE_PRIVATE)
        root.playButton.setOnClickListener { view ->
            play(root)
        }
        root.pauseButton.setOnClickListener {view ->
            pause(root)
        }
        radio.setConfig("GBR", "1980", "FAST")
        return root
    }

    // для кнопочек
    private fun pause(view: View) {
        Log.d("debug", "pause btn")
        musicPlayer.pause()
    }

    private fun play(view: View) {
        Log.d("debug", "play btn")
        var country = prefs.getString(countryKey, "") as String
        var year = prefs.getString(yearKey, "") as String
        var mood = prefs.getString(moodKey, "") as String
        Toast.makeText((this.context as Context), mood + " " + country + " " + year, Toast.LENGTH_LONG).show()
        if(!musicPlayer.isPlaying()) {
            radio.getNextSongUrl(mood, country, year)
            musicPlayer.play()
        }
    }
}