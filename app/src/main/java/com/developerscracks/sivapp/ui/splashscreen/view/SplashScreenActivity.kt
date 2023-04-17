package com.developerscracks.sivapp.ui.splashscreen.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.developerscracks.sivapp.R
import com.developerscracks.sivapp.databinding.ActivitySplashScreenBinding
import com.developerscracks.sivapp.ui.main.MainActivity
import com.developerscracks.sivapp.utils.Constants.DURACION_SPLASH_SCREEN

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Ocultar la barra de acciones
        supportActionBar!!.hide()

        //Ocultar la barra de estado
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Glide.with(this).load(R.drawable.dispositivos).centerCrop().into(binding.ivSplashScreen)

        changeScreen()
    }

    private fun changeScreen(){
        //Definimos la duración en que se mostrara el splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, DURACION_SPLASH_SCREEN)
    }
}