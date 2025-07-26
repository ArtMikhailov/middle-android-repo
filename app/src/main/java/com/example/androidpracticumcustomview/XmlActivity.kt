package com.example.androidpracticumcustomview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.ViewCompat
import androidx.core.view.ViewGroupCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidpracticumcustomview.ui.theme.CustomContainer

class XmlActivity : ComponentActivity() {
    private var addSecondViewHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startXmlPracticum(savedInstanceState)
    }

    private fun startXmlPracticum(savedInstanceState: Bundle?) {
        val customContainer = CustomContainer(this)
        setContentView(customContainer)
        supportEdgeToEdgeScreen(customContainer)
        customContainer.setOnClickListener {
            finish()
        }
        customContainer.setAnimationEnabled(savedInstanceState == null)
        val firstView = TextView(this).apply {
            text = getString(R.string.custom_view_group_first_view_name)
            textSize = 20f
        }
        val secondView = TextView(this).apply {
            text = getString(R.string.custom_view_group_second_view_name)
            textSize = 20f
        }
        customContainer.addView(firstView)
        if (savedInstanceState != null) {
            customContainer.addView(secondView)
        } else {
            addSecondViewHandler.postDelayed({
                customContainer.addView(secondView)
            }, 2000)
        }
    }

    private fun supportEdgeToEdgeScreen(view: View) {
        ViewGroupCompat.installCompatInsetsDispatch(view)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val innerPadding = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            v.setPadding(
                innerPadding.left,
                innerPadding.top,
                innerPadding.right,
                innerPadding.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addSecondViewHandler.removeCallbacksAndMessages(null)
    }
}