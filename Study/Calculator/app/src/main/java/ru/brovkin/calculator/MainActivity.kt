package ru.brovkin.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sun.text.normalizer.UTF16.append
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {

    var numberField : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onNumberClick(view: View) {

        val button = view as Button
        var txtButton : String = button.getText() as String

        if (txtButton == "1"){
            tx
        }
        if (lastOperation.equals("=") && operand != null) {
            operand = null
        }
    }
}
