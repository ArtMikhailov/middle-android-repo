package com.example.androidpracticumcustomview

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.ViewGroupCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportEdgeToEdgeScreen()
        val buttonXml = findViewById<Button>(R.id.xmlActivity)
        val buttonCompose = findViewById<Button>(R.id.composeActivity)
        buttonXml.setOnClickListener {
            startActivity(Intent(this, XmlActivity::class.java))
        }
        buttonCompose.setOnClickListener {
            startActivity(Intent(this, ComposeScreen::class.java))
        }
    }

    private fun supportEdgeToEdgeScreen() {
        val rootView = findViewById<View>(R.id.rootView)
        ViewGroupCompat.installCompatInsetsDispatch(rootView)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val innerPadding = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(
                innerPadding.left,
                innerPadding.top,
                innerPadding.right,
                innerPadding.bottom
            )
            WindowInsetsCompat.CONSUMED
        }
    }
}