package com.example.timeyradio.ui.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.example.timeyradio.MusicPlayer
import com.example.timeyradio.R
import com.example.timeyradio.Radiooooo
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment()  {
    private lateinit var prefs: SharedPreferences
    val settingsViewModel: SettingsViewModel by navGraphViewModels<SettingsViewModel>(R.id.mobile_navigation)
    var is_stop = false
    var current_selected = "123"
    val countryKey = "COUNTRY_KEY"
    val yearKey = "YEAR_KEY"
    val moodKey = "MOOD_KEY"
//    в данной реализации присутсвует куча говно-кода, чтобы сохранять текущие настройки
//    чтобы получить текущиие значения настроек, нужно сначала создать экзмеляр SharedPreferences,
//    prefs = (this.activity as Activity).getSharedPreferences("settings", Context.MODE_PRIVATE)
//    после чего взять по ключу нужную опцию prefs.getString(countryKey, "") as String
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        prefs = (this.activity as Activity).getSharedPreferences("settings", Context.MODE_PRIVATE)
        Toast.makeText((this.context as Context), "onRestored " + prefs.getString(countryKey, ""), Toast.LENGTH_LONG).show()
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("IS_PAUSE", 1)
//        outState.putString("CURRENT_SELECTED", settingsViewModel.text1.value)
//        Toast.makeText((this.context as Context),"123" , Toast.LENGTH_LONG).show()
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {

        super.onViewStateRestored(savedInstanceState)
//        Toast.makeText((this.context as Context), "onRestored " + settingsViewModel.text1.value, Toast.LENGTH_LONG).show()
        is_stop = true

    }
    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()

        initCountries()
        initMood()
        initYear()

    }

    @SuppressLint("CommitPrefEdits")
    override fun onPause() {
        super.onPause()

    }


    @SuppressLint("SetTextI18n")
    fun initCountries(){
        settingsViewModel.currentCountry.observe(viewLifecycleOwner, Observer {
            textCountry.text = "Выбрано : $it"
        })
        val countries = settingsViewModel.defCountries
        val adapter = ArrayAdapter(this.context as Context, android.R.layout.simple_spinner_dropdown_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinnerCountry.adapter = adapter

        spinnerCountry?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            @SuppressLint("CommitPrefEdits")
            override fun onItemSelected(parent:AdapterView<*>, view: View?, position: Int, id: Long){
                if(!(is_stop)){
                    if(parent.getItemAtPosition(position) != null) {
                        settingsViewModel.saveCurrentCountry(
                            parent.getItemAtPosition(position).toString()
                        )
                        val editor = prefs.edit()
//                        editor.clear()
                        editor.putString(countryKey, settingsViewModel.currentCountry.value.toString()).apply()
                    }
                }
//                is_stop = false
                if(prefs.contains(countryKey))
                    settingsViewModel.saveCurrentCountry(prefs.getString(countryKey, "") as String)
                spinnerCountry?.setSelection(adapter.getPosition(settingsViewModel.currentCountry.value.toString()))
            }
            override fun onNothingSelected(parent: AdapterView<*>){

            }
        }
    }

    fun initMood(){
        settingsViewModel.currentMood.observe(viewLifecycleOwner, Observer {
            textMood.text = "Выбрано : $it"
        })
        val moods = settingsViewModel.defMoods
        val adapter = ArrayAdapter(this.context as Context, android.R.layout.simple_spinner_dropdown_item, moods)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinnerMood.adapter = adapter

        spinnerMood?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            @SuppressLint("CommitPrefEdits")
            override fun onItemSelected(parent:AdapterView<*>, view: View?, position: Int, id: Long){
                if(!(is_stop)){
                    if(parent.getItemAtPosition(position) != null) {
                        settingsViewModel.saveCurrentMood(
                            parent.getItemAtPosition(position).toString()
                        )
                        val editor = prefs.edit()
//                        editor.clear()
                        editor.putString(moodKey, settingsViewModel.currentMood.value.toString()).apply()
                    }
                }
//                is_stop = false
                if(prefs.contains(moodKey))
                    settingsViewModel.saveCurrentMood(prefs.getString(moodKey, "") as String)
                spinnerMood?.setSelection(adapter.getPosition(settingsViewModel.currentMood.value.toString()))
            }
            override fun onNothingSelected(parent: AdapterView<*>){

            }
        }
    }

    fun initYear(){
        settingsViewModel.currentYear.observe(viewLifecycleOwner, Observer {
            textYear.text = "Выбрано : $it"
        })
        val years = settingsViewModel.defYears
        val adapter = ArrayAdapter(this.context as Context, android.R.layout.simple_spinner_dropdown_item, years)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinnerYear.adapter = adapter

        spinnerYear?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            @SuppressLint("CommitPrefEdits")
            override fun onItemSelected(parent:AdapterView<*>, view: View?, position: Int, id: Long){
                if(!(is_stop)){

                    if(parent.getItemAtPosition(position) != null){
                        settingsViewModel.saveCurrentYear(parent.getItemAtPosition(position).toString())
                        val editor = prefs.edit()
//                        editor.clear()
                        editor.putString(yearKey, settingsViewModel.currentYear.value.toString()).apply()
                    }
                }
                is_stop = false
            }
            override fun onNothingSelected(parent: AdapterView<*>){

            }
        }
        if(prefs.contains(yearKey))
            settingsViewModel.saveCurrentYear(prefs.getString(yearKey, "") as String)

        spinnerYear?.setSelection(adapter.getPosition(settingsViewModel.currentYear.value.toString()))
    }
}