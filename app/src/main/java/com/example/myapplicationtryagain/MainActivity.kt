package com.example.myapplicationtryagain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.myapplicationtest.TestFunction
import com.example.myapplicationtryagain.navigation.AppNavGraph
import com.example.myapplicationtryagain.ui.theme.MyApplicationTryAgainTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTryAgainTheme {
                AppNavGraph()
            }
        }
    }
}
