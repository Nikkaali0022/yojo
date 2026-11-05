package com.yojo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yojo.app.ui.YojoRoot
import com.yojo.app.ui.theme.YojoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { YojoTheme { YojoRoot() } }
    }
}
