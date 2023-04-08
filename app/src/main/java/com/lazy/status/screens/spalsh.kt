package com.lazy.status.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.belazy.lazystatus.R
import com.lazy.status.MainActivity

class spalsh : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)

         Handler(Looper.getMainLooper()).postDelayed({
           val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}