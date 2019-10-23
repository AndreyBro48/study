package ru.brovkin.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*


class MainActivity : AppCompatActivity() {

    lateinit var numberField : TextView
    val operationWithTwoOperands: Array<String> = arrayOf(" + ", " - ", " AND ", " OR ", " XOR ")
    val operationWithOneOperand: Array<String> = arrayOf("INV", "<<", ">>");

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        numberField = this.findViewById(R.id.idNumberField)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("numberField",numberField.text.toString())
        outState.putString("txtAnswer", txtAnswer.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        numberField.text = savedInstanceState["numberField"] as String
        txtAnswer.text = savedInstanceState["txtAnswer"] as String
    }
    fun onNumberAndOperationClick(view: View) {
        val button = view as Button
        val txtButton : String = button.text.toString()

        if (txtButton == "DEL"){
            if (numberField.text.last() == ' ') {
                numberField.text =
                    numberField.text.substring(0, numberField.text.indexOf(' '))
            } else if (numberField.text.count() > 1) {
                numberField.text = numberField.text.substring(0, numberField.text.length - 1)
            } else{
                numberField.text = ""
            }
        } else if (txtButton == "1" || txtButton == "0") {
            if (numberField.text == "0")
                numberField.text = ""
            numberField.append(txtButton)
        } else {
            if (numberField.text.last() == ' ')
            {
                numberField.text =
                    numberField.text.substring(0, numberField.text.indexOf(' '))
            } else if (numberField.text.split(" ").count() == 3){
                numberField.text = solve()
            }

            if (operationWithTwoOperands.contains(txtButton)) {
                numberField.append(txtButton)
            } else {
                numberField.text = solve()
                val txt = numberField.text.toString()
                if (txtButton == "<<"){
                    numberField.text = txt.toInt(2).shl(1).toString(2)
                } else if (txtButton == ">>"){
                    numberField.text = txt.toInt(2).shr(1).toString(2)
                } else if (txtButton == "INV"){
                    var num = txt.toUInt(2).inv().toString(2)
                    numberField.text = num.substring(num.length-txt.length).toInt(2).toString(2)
                }
            }
        }

        txtAnswer.text = solve()
    }

    fun onSolve(view: View) {
        numberField.text = solve()
    }

    fun solve(): String {
        val txt = numberField.text.toString()
        val vars = txt.split(" ")
        if (vars.count()==3)
        {
            if (vars[2] == ""){
                return vars[0]
            }
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
        return txt
    }
}


