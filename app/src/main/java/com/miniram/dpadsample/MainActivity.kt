package com.miniram.dpadsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dpad.offerwall.DPAD

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DPAD.init(this, "6t4dzjhs", "rbtgcx5wyqv0iomeswv3kyjjbhskdz", System.currentTimeMillis().toString());
        findViewById<View>(R.id.button1).setOnClickListener {
            DPAD.showOfferwall(this@MainActivity)
        }
    }
}