package com.example.exintentfilter

import android.graphics.drawable.Drawable
import java.io.Serializable

data class MyData(var applabel :String,var appclass:String,var apppackagename:String,var appicon:Drawable) :Serializable {
}