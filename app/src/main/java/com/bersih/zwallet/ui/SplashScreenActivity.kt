package com.bersih.zwallet.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bersih.zwallet.R
import com.bersih.zwallet.ui.auth.AuthActivity
import com.bersih.zwallet.ui.main.profile.MainActivity
import com.bersih.zwallet.utils.KEY_LOGGED_IN
import com.bersih.zwallet.utils.PREFS_NAME

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        if (prefs.getBoolean(KEY_LOGGED_IN, false)) {
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 800)
        } else {
            Handler().postDelayed({
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }, 800)
        }
    }
}