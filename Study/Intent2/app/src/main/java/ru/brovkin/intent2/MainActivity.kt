package ru.brovkin.intent2

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                txtOne.text = "Получено сообщение: ${data?.getStringExtra(Intent.EXTRA_TEXT)}"
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Figure out what to do based on the intent type
        if (intent?.type == "text/plain") {
            txtOne.text = "Получено сообщение: ${intent?.getStringExtra(Intent.EXTRA_TEXT)}"
        }
    }

    public fun sendMsg(view : View){
        var intent = Intent()
        intent.putExtra(Intent.EXTRA_TEXT, editText.text.toString())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
