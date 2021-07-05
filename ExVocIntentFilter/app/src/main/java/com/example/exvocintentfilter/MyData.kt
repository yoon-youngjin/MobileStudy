package com.example.exvocintentfilter

import android.graphics.drawable.Drawable
import java.io.Serializable

data class MyData(var applabel:String, var appclass:String, var apppackname:String,var appicon:Drawable) :Serializable {
    // name : text뷰에 출력할 텍스트 , pt : 글자크기정보

}
