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
    val operationWithTwoOperands: Array<String> = arrayOf(" + ", " - ", " AND ", " OR ", " XOR ")
    val operationWithOneOperand: Array<String> = arrayOf("INV", "<<", ">>");

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        numberField = this.findViewById(R.id.idNumberField)
    }

    fun onNumberClick(view: View) {
        val button = view as Button
        var txtButton : String = button.text as String

        if (txtButton == "1" || txtButton == "0") {
            numberField.append(txtButton)
        } else if (operationWithTwoOperands.contains(txtButton)) {
            if (numberField.text.last() == ' ')
            {
                numberField.text =
                    numberField.text.substring(0, numberField.text.indexOf(' '));
            } 
            numberField.append(txtButton)
        } else if (txtButton == "<<"){

        }

    }

    fun onSolve(view: View) {
        val solveCurExpression = solve()


    }

    fun solve(): String {
        val txt = numberField.text
        val vars = txt.split(" ");
        if (vars.count()==3)
        {
            val num1 = vars[0].toInt(2)
            val num2 = vars[2].toInt(2)
            if (txt.contains('+')){
                return (num1+num2).toString(2)
            } else if (txt.contains('-')){
                return (num1-num2).toString(2)
            } else if (txt.contains("XOR")){
                return num1.xor(num2).toString(2)
            } else if (txt.contains("AND")){
                return num1.and(num2).toString(2)
            } else if (txt.contains("OR")){
                return num1.or(num2).toString(2)
            }
        }
        return txt as String
    }
}


