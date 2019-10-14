package ru.brovkin.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    lateinit var numberField : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        numberField = this.findViewById(R.id.idNumberField)
    }

    fun onNumberClick(view: View) {
        val button = view as Button
        var txtButton : String = button.text as String

        if (txtButton == "1" || txtButton == "0"){
            numberField.append(txtButton)
        }

    }

    fun onSolve(view: View) {
        solve()
    }

    fun solve(){
        val txt = numberField.text
        val vars = txt.split(" + "," - ")
        if (vars.count()==2)
        {
            val num1 = vars[0].toInt(2);
            val num2 = vars[1].toInt(2);
            if (txt.contains('+'))
            {
                numberField.text = (num1+num2).toString(2);
            }
            else
            {
                numberField.text = (num1-num2).toString(2);
            }
        }

    }
}

