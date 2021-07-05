package com.example.completetextview

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {
    var countries2 = mutableListOf("Aasdf","Asdvd","adsfas","basdfa")
    lateinit var  adapter : ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    fun init() {
        val autoText = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val mulautoText = findViewById<MultiAutoCompleteTextView>(R.id.multiAutoCompleteTextView)
        mulautoText.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        val country = resources.getStringArray(R.array.country_name)

        val btn = findViewById<Button>(R.id.button)
        val editText = findViewById<EditText>(R.id.editText)

//                editText.addTextChangedListener(object :TextWatcher {
//                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//                    }
//
//                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    }
//
//                    override fun afterTextChanged(s: Editable?) {
//                        val addText = s.toString()
//                        btn.isEnabled = addText.isNotEmpty()
//                    }
//
//                })
//        editText.addTextChangedListener(
//                afterTextChanged = {
//                    val addText = it.toString()
//                    btn.isEnabled = addText.isNotEmpty()
//                }
//        )
//
//        btn.setOnClickListener {
//            adapter.add(editText.text.toString())
//            adapter.notifyDataSetChanged()
//            editText.text.clear()
//        }

        editText.addTextChangedListener {
            val str = it.toString();
            btn.isEnabled = str.isNotEmpty()
        }
        btn.setOnClickListener {
            adapter.add(editText.text.toString())
            adapter.notifyDataSetChanged()
            editText.text.clear()
        }


        adapter = ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,countries2)
        autoText.setAdapter(adapter)
        mulautoText.setAdapter(adapter)
    }
}