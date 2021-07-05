package com.example.midproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.midproject.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    val REQUEST_CODE= 100
    lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    inner class CustomOnItemSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (parent?.selectedItem) {
                "대한전자" -> {
                    binding.button.setOnClickListener {
                        val intent = Intent(this@IntroActivity,MainActivity::class.java)
                        startActivityForResult(intent,REQUEST_CODE)
                    }
                }


            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_CODE-> {
                if(resultCode == Activity.RESULT_OK) {
                    val str = data?.getSerializableExtra("data") as MyData
                    binding.modelName.setText(str.model)
                    binding.editTextTextPersonName2.setText(str.a)
                    binding.editTextTextPersonName2.setText(str.cost)

                    Toast.makeText(this, str.model, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun init() {

        binding.spinner.setOnItemSelectedListener(CustomOnItemSelectedListener())
        binding.button.setOnClickListener {

        }

    }
}




