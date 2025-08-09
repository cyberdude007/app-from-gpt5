
package com.example.splitpaisa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.splitpaisa.ui.theme.SplitPaisaTheme
import com.example.splitpaisa.nav.AppNav
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplitPaisaTheme { AppNav() }
        }
    }
}
