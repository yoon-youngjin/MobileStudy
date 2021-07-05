package com.example.mp3player

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import java.lang.Math.PI
import kotlin.math.atan2

class VoulmeControlView(context: Context,attrs:AttributeSet) : AppCompatImageView(context,attrs) {
    var mx = 0.0f
    var my = 0.0f
    var tx = 0.0f
    var ty = 0.0f
    var angle = 0.0f
    var listener :VolumeListener ?= null

    public interface VolumeListener {
        public fun onChanged(angle:Float) {}
    }

    public fun setVolumeListener(listener: VolumeListener) {
        this.listener = listener
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event!=null) {
            mx = event.getX(0)
            my = event.getY(0)
            angle = getAngle(mx,my)
            invalidate()
            listener?.onChanged(angle)
            return true
        }
        else
            return false

    }

    fun getAngle(x1:Float, y1:Float):Float {
        tx = x1-(width/2.0f)
        ty = (height/2.0f) -y1
        return (atan2(mx,my)*180.0f/PI).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.rotate(angle, (width/2.0f), (height/2.0f))
        super.onDraw(canvas)
    }


}