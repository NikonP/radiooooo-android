package com.example.timeyradio.ui.player

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.core.net.toUri
import com.example.timeyradio.MusicPlayer
import com.example.timeyradio.R
import com.example.timeyradio.Radiooooo
import kotlinx.android.synthetic.main.fragment_player.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget


//выф
class PlayerFragment : Fragment() {

    private lateinit var playerViewModel: PlayerViewModel
    private val musicPlayer: MusicPlayer = MusicPlayer()
    private val radio: Radiooooo = Radiooooo()
    private lateinit var prefs: SharedPreferences
    private val countryKey = "COUNTRY_KEY"
    private val yearKey = "YEAR_KEY"
    private val moodKey = "MOOD_KEY"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        playerViewModel =
                ViewModelProvider(this).get(PlayerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_player, container, false)
        prefs = (this.activity as Activity).getSharedPreferences("settings", Context.MODE_PRIVATE)
        root.playPauseButton.setOnClickListener { view ->
            playPause(root)
        }
        root.nextTrackButton.setOnClickListener { view ->
            nextTrack(root)
        }
        root.prevTrackButton.setOnClickListener {
            musicPlayer.switchLoop()
        }
        val country = prefs.getString(countryKey, "") as String
        val year = prefs.getString(yearKey, "") as String
        val mood = prefs.getString(moodKey, "") as String
        radio.getNextSongUrl(mood, country, year)

        GlobalScope.launch {
            while (radio.getCurrentSong().url.isEmpty()) {}
            val currentSong = radio.getCurrentSong()
            musicPlayer.setSong(currentSong)
            root.post( Runnable {
                updateText(root.songName, currentSong.title)
                updateText(root.songArtist, currentSong.artist)
            })
            updateCover(root.albumCover, currentSong.imgUrl)
            val splashScreen = childFragmentManager.findFragmentById(R.id.splashArtFragment)
            if (splashScreen != null) {
                Log.d("debug", "found the fragment in ${splashScreen.parentFragment.toString()}")
                childFragmentManager.beginTransaction().hide(splashScreen).commit()
            }
            radio.getNextSongUrl(mood, country, year)
        }
        return root
    }

    override fun onStop() {
        musicPlayer.pause()
        musicPlayer.release()
        super.onStop()
    }

    // для кнопочек
    private fun playPause(view: View) = if (musicPlayer.isPlaying()) {
        Log.d("debug", "pause btn")
        musicPlayer.pause()
        view.playPauseButton.setImageResource(R.drawable.ic_media_play)
    } else {
        Log.d("debug", "play btn")
        musicPlayer.play()
        view.playPauseButton.setImageResource(R.drawable.ic_media_pause)
    }

    private fun nextTrack(view: View) {
        val country = prefs.getString(countryKey, "") as String
        val year = prefs.getString(yearKey, "") as String
        val mood = prefs.getString(moodKey, "") as String
        Toast.makeText(
            (this.context as Context),
            "$mood $country $year",
            Toast.LENGTH_LONG
        ).show()
        val currentSong = radio.getCurrentSong()
        view.albumCover.setImageResource(R.drawable.cover_template)
        updateText(view.songName, currentSong.title)
        updateText(view.songArtist, currentSong.artist)
        updateCover(view.albumCover, currentSong.imgUrl)
        radio.getNextSongUrl(mood, country, year)

        if (musicPlayer.isPlaying()) {
            musicPlayer.reset()
            musicPlayer.setSong(currentSong)
            musicPlayer.play()
        } else {
            musicPlayer.reset()
            musicPlayer.setSong(currentSong)
        }
    }

    private fun updateCover(view: ImageView, url: String) {
        val thread = Thread( Runnable {
            try {
                val imgUri = url.toUri().buildUpon().scheme("https").build()
                Glide.with(view.context)
                    .asBitmap()
                    .load(imgUri)
                    .into(BitmapImageViewTarget(view))
            } catch (th: Throwable) {
                Log.d("debug", "album update error: $th")
            }
        })
        thread.start()
    }

    private fun updateText(view: TextView, str: String) {
        view.setText(str)
    }
}