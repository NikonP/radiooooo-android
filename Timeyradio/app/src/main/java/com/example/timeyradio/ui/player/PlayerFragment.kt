package com.example.timeyradio.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.timeyradio.MusicPlayer
import com.example.timeyradio.R
import com.example.timeyradio.Radiooooo

//выф
class PlayerFragment : Fragment() {

    private lateinit var playerViewModel: PlayerViewModel
    private val musicPlayer: MusicPlayer = MusicPlayer()
    private val radio: Radiooooo = Radiooooo(musicPlayer)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        playerViewModel =
                ViewModelProvider(this).get(PlayerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_player, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        playerViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    // для кнопочек
    fun pause(view: View) {

    }

    fun play(view: View) {

    }
}