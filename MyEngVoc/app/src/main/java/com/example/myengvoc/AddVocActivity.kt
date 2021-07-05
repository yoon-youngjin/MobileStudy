package com.example.myengvoc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.io.PrintStream

class AddVocActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_voc)
        init()
    }

    private fun init() {
        val wordEdit = findViewById<EditText>(R.id.word)
        val meaningEdit = findViewById<EditText>(R.id.meaning)
        val btn1 = findViewById<Button>(R.id.button3)
        val btn2 = findViewById<Button>(R.id.button4)

        btn1.setOnClickListener {
            val word = wordEdit.text.toString()
            val meaning = meaningEdit.text.toString()
            writeFile(word,meaning)
        }
        btn2.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun writeFile(word: String, meaning: String) {
        val output = PrintStream(openFileOutput("out.txt", Context.MODE_APPEND))
        output.println(word)
        output.println(meaning)

        val intent = Intent()
        intent.putExtra("voc",MyData(word,meaning))
        setResult(Activity.RESULT_OK,intent)
        finish()

    }

    }

