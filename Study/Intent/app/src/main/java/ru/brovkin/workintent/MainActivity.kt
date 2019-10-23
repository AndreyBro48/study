package ru.brovkin.workintent

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {

    val PICK_CONTACT_REQUEST = 1  // The request code
    private fun pickContact() {
        Intent(Intent.ACTION_PICK, Uri.parse("content://contacts")).also { pickContactIntent ->
            pickContactIntent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE // Show user only contacts w/ phone numbers
            startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                txtOne.text = "Получено сообщение: ${data?.getStringExtra(Intent.EXTRA_TEXT)}"
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    fun isIntentAvailable(context: Context, intent: Intent): Boolean {
        val packageManager = context.packageManager
        val list = packageManager.queryIntentActivities(
            intent, PackageManager.MATCH_DEFAULT_ONLY
        )
        return list.size > 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val context : Context = applicationContext

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data: Uri? = intent?.data

        // Figure out what to do based on the intent type
        if (intent?.type == "text/plain") {
            txtOne.text = "Получено сообщение: ${data.toString()}"
        }
    }
    fun request(view : View) {
        startActivityForResult(Intent("ru.brovkin.workintent.MainActivity"),1)
    }
    fun sendMsg(view : View){
        var intent = packageManager.getLaunchIntentForPackage("ru.brovkin.intent2")
        intent!!.action = Intent.ACTION_MAIN
        intent!!.putExtra(Intent.EXTRA_TEXT, editText.text.toString())
        intent!!.type = "text/plain"

        startActivity(intent)
    }
}
