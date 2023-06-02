package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app.databinding.ActivitySplashScreenBinding

class Splash_Screen : AppCompatActivity() {
    lateinit var binding :ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        InterstitialClass.load_interstitial_splash(this){

        }


        binding.btnStart.setOnClickListener{

            var intent=Intent(this@Splash_Screen, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}