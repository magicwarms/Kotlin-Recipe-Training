package com.example.myawesomerecipe

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android Mantap ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()