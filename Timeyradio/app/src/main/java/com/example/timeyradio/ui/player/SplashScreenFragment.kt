package com.example.timeyradio.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.timeyradio.R
import kotlinx.android.synthetic.main.splash_art.view.*
import kotlinx.coroutines.runBlocking

class SplashScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.splash_art, container, false)
        root.myImageView.setImageResource(R.drawable.radioooooooooo)
        return root
    }

}