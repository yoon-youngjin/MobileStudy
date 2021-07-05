package com.example.completetext

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.MultiAutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var adapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        val autoComText = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val multiComText = findViewById<MultiAutoCompleteTextView>(R.id.multiAutoCompleteTextView)
        multiComText.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        val countries = resources.getStringArray(R.array.country_name)

        adapter = ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,countries)
        autoComText.setAdapter(adapter)
        multiComText.setAdapter(adapter)
    }
}