package com.example.myrecyclerview

import android.graphics.drawable.Drawable
import java.io.Serializable

data class MyData(val applabel:String,val appclass:String,val apppackagename:String,val appicon:Drawable) :Serializable {}
