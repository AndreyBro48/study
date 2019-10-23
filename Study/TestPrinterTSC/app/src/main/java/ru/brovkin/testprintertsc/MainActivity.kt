package ru.brovkin.testprintertsc

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.tscdll.TSCActivity
import com.example.tscdll.TSCUSBActivity
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {


    //internal var TscUSB = TSCUSBActivity()

    private lateinit var txtLog : TextView
    private var mUsbIntf: UsbInterface? = null
    private var mUsbendpoint: UsbEndpoint? = null
    private var mUsbConnection: UsbDeviceConnection? = null
    private var usbEndpointOut: UsbEndpoint? = null
    private var usbEndpointIn: UsbEndpoint? = null
    private val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    private var mUsbManager: UsbManager? = null
    private var mPermissionIntent: PendingIntent? = null
    private var hasPermissionToCommunicate = false

    private var test: Button? = null
    private var tv1: TextView? = null
    private var device: UsbDevice? = null


    internal var filterAttached_and_Detached = IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED)
    // Catches intent indicating if the user grants permission to use the USB device
    private val mUsbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (ACTION_USB_PERMISSION == action) {
                synchronized(this) {
                    val device = intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            hasPermissionToCommunicate = true
                        }
                    }
                }
            }
        }
    }

    fun openport(manager: UsbManager, device: UsbDevice): String {
        mUsbIntf = device.getInterface(0)
        mUsbendpoint = mUsbIntf?.getEndpoint(0)
        mUsbConnection = manager.openDevice(device)
        val port_status = this.mUsbConnection!!.claimInterface(mUsbIntf, true)

        for (i in 0 until mUsbIntf!!.endpointCount) {
            val end = mUsbIntf!!.getEndpoint(i)
            if (end.direction == 128) {
                usbEndpointIn = end
            } else {
                usbEndpointOut = end
            }
        }

        try {
            Thread.sleep(100L)
        } catch (var6: InterruptedException) {
            var6.printStackTrace()
        }

        return if (port_status) {
            "1"
        } else {
            "-1"
        }
    }

    fun sendcommand(printercommand: String): String {
        if (mUsbConnection == null) {
            return "-1"
        } else {
            try {
                Thread.sleep(100L)
            } catch (var6: InterruptedException) {
            }
            //val command = printercommand.toByteArray()
            //val tmp = mUsbConnection!!.bulkTransfer(mUsbendpoint,command,command.size,100)
            val thread = Thread(Runnable {
                val command = printercommand.toByteArray(Charset.forName("windows-1251"))
                mUsbConnection!!.bulkTransfer(
                    mUsbendpoint,
                    command,
                    command.size,
                    100
                )
            })
            thread.start()

            try {
                thread.join()
            } catch (var5: InterruptedException) {
                var5.printStackTrace()
            }

            try {
                Thread.sleep(50L)
            } catch (var4: InterruptedException) {
            }

            return "-1"
//            return tmp.toString()
        }
    }

    fun closeport(timeout: Int): String {
        if (mUsbConnection == null) {
            return "-1"
        } else {
            try {
                Thread.sleep(timeout.toLong())
            } catch (var4: InterruptedException) {
                var4.printStackTrace()
            }

            try {
                mUsbConnection?.close()
                mUsbConnection?.releaseInterface(this.mUsbIntf)
                //mUsbConnection = null
               // mUsbendpoint = null
                //this.mUsbManager = null
                //this.mUsbIntf = null
                txtLog.append("DemoKit: Device closed.\r\n")
            } catch (var3: Exception) {
                txtLog.append("DemoKit: Exception: \" + var3.message\r\n")
            }

            return "1"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtLog = findViewById(R.id.textView)
        test = findViewById(R.id.button)

        mUsbManager = getSystemService(Context.USB_SERVICE) as UsbManager
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, Intent(ACTION_USB_PERMISSION), 0)
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        registerReceiver(mUsbReceiver, filter)


        val accessoryList = mUsbManager!!.accessoryList
        val deviceList = mUsbManager!!.deviceList

        txtLog.append("Detect: " + deviceList.size.toString() + " USB device(s) found\r\n")

        if (deviceList.size != 0) {
            val deviceIterator = deviceList.values.iterator()
            while (deviceIterator.hasNext()) {
                device = deviceIterator.next()
                txtLog.append("device.vendorId: ${device?.serialNumber}\r\n")
                txtLog.append("device.productId: ${device?.serialNumber}\r\n")

                if (device!!.vendorId == 0x1203 && device!!.productId == 0x160) {
                    txtLog.append("SerialNumber ${device?.serialNumber}\r\n")
                    //Toast.makeText(MainActivity.this, device.toString(), 0).show();
                    break
                }
            }

            //-----------start-----------
            val mPermissionIntent: PendingIntent
            mPermissionIntent = PendingIntent.getBroadcast(
                this@MainActivity, 0,
                Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_ONE_SHOT
            )
            mUsbManager!!.requestPermission(device, mPermissionIntent)
            test!!.setOnClickListener(View.OnClickListener {
                if (mUsbManager!!.hasPermission(device)) {

                    //String status = TscUSB.printerstatus(300);

                    this.openport(mUsbManager!!, device!!)
                    var comands =
                                        "CODEPAGE 1251\r\n" +
                                        "CLS\r\n" +
                                        "SIZE 60 mm, 59 mm\r\n" +
                                        "SPEED 4\r\n" +
                                        "DENSITY 12\r\n" +
                                        "PUTPCX 0,6,\"EAC.PCX\"\r\n" +
                                        "FONTMAIN\$=\"TNR13\"\r\n" +
                                        "FONTHEADER\$=\"TNR16\"\r\n" +
                                        "TEXT 44,0,FONTMAIN\$,0,1,1,\"8G3ET1212_19P_905_36\"\r\n" +
                                        "TEXT 44,25,FONTMAIN\$,0,1,1,\"Артикул\"\r\n" +
                                        "TEXT 182,25,FONTMAIN\$,0,1,1,2,\"кроссовки\"\r\n" +
                                        "TEXT 285,0,FONTMAIN\$,0,1,1,2,\"43,5\"\r\n" +
                                        "TEXT 285,25,FONTMAIN\$,0,1,1,2,\"Размер\"\r\n" +
                                        "TEXT 376,0,FONTMAIN\$,0,1,1,2,\"09.2019\"\r\n" +
                                        "TEXT 376,25,FONTMAIN\$,0,1,1,2,\"Произведено\"\r\n" +
                                        "DIAGONAL 114,25,114,48,3\r\n" +
                                        "DIAGONAL 250,5,250,48,3 \r\n" +
                                        "DIAGONAL 320,5,320,48,3 \r\n" +
                                        "DIAGONAL 0,48,416,48,2 \r\n" +
                                        "MAININFO = 60\r\n" +
                                        "TEXT 0,MAININFO,FONTMAIN\$,0,1,1,\"Беговые кроссовки ASICS Patriot 10 — надежная модель\"\r\n" +
                                        "TEXT 0,MAININFO + 14,FONTMAIN\$,0,1,1,\"для регулярных пробежек по шоссе. Верх кроссовок\"\r\n" +
                                        "TEXT 0,MAININFO + 14*2,FONTMAIN\$,0,1,1,\"выполнен из дышащей сетчатой ткани, которая \"\r\n" +
                                        "TEXT 0,MAININFO + 14*3,FONTMAIN\$,0,1,1,\"обеспечивает вентиляцию и комфорт. Эту модель \"\r\n" +
                                        "TEXT 0,MAININFO + 14*4,FONTMAIN\$,0,1,1,\"отличает амортизированная промежуточная подошва \"\r\n" +
                                        "TEXT 0,MAININFO + 14*5,FONTMAIN\$,0,1,1,\"из материала AmpliFoam. А съемные стельки EVA\"\r\n" +
                                        "TEXT 0,MAININFO + 14*6,FONTMAIN\$,0,1,1,\"адаптируются к индивидуальным особенностям стопы\"\r\n" +
                                        "TEXT 0,MAININFO + 14*7,FONTMAIN\$,0,1,1,\"и дарят комфорт во время бега на короткие дистанции.\"\r\n" +
                                        "TEXT 0,180,FONTMAIN\$,0,1,1,\"Верх: Тесктиль\"\r\n" +
                                        "TEXT 0,200,FONTMAIN\$,0,1,1,\"Подкладка: Тесктиль\"\r\n" +
                                        "TEXT 0,220,FONTMAIN\$,0,1,1,\"Подошва: Резина\"\r\n" +
                                        "TEXT 0,240,FONTMAIN\$,0,1,1,\"Цвет: Очень светлый желто-зелёный\"\r\n" +
                                        "TEXT 0,260,FONTMAIN\$,0,1,1,\"Производитель: SHENZHEN BEST\"\r\n" +
                                        "TEXT 0,260+14,FONTMAIN\$,0,1,1,\"AGES FOOTWEAR CO., LTD., Китай,\"\r\n" +
                                        "TEXT 0,260+14*2,FONTMAIN\$,0,1,1,\"Garden A Junjinghaoting, Yanbu\"\r\n" +
                                        "TEXT 0,260+14*3,FONTMAIN\$,0,1,1,\"Dall, Nanhai AGES FOOTWEAR CO.,\"\r\n" +
                                        "TEXT 0,260+14*4,FONTMAIN\$,0,1,1,\"LTD., Китай, Garden GD ,\"\r\n" +
                                        "TEXT 0,260+14*5,FONTMAIN\$,0,1,1,\"Junjinghaoting Yanbu Dall, Nanhai\"\r\n" +
                                        "TEXT 0,350,FONTMAIN\$,0,1,1,   \"Импортер: ООО \\\"Обувной рай\\\",\"\r\n" +
                                        "TEXT 0,350+14,FONTMAIN\$,0,1,1,\"102030, Россия, Московская область,\"\r\n" +
                                        "TEXT 0,350+14*2,FONTMAIN\$,0,1,1,\"Химки, Куркинское шоссе, строение\"\r\n" +
                                        "TEXT 0,350+14*3,FONTMAIN\$,0,1,1,\"223, тел. 8 (999) 888-77-66\"\r\n" +
                                        "TEXT 0,410,FONTMAIN\$,0,1,1,\"РЕСПУБЛИКА МАКЕДОНИЯ, БЫВШАЯ\"\r\n" +
                                        "TEXT 0,424,FONTMAIN\$,0,1,1,\"ЮГОСЛАВСКАЯ РЕСПУБЛИКА\"\r\n" +
                                        "DMATRIX 270,190,180,180,x4,\"010463003965139121demo8KD2SddK291ffd092demowhrzOhg47sToY3HLwOL3hKy+K1e8eoV6KOfLQRLEJQYWiQ2VbSaq+Hh3mz0kgIPQ/4mVcLtf179bIIcQJg==\"\r\n" +
                                        "TEXT 275,360,FONTMAIN\$,0,1,1,\"GTIN: 04650075190142\"\r\n" +
                                        "TEXT 275,374,FONTMAIN\$,0,1,1,\"s/n:  demo35bf77604\"\r\n" +
                                        "PRINT 1\r\n" +
                                        "END\r\n"

                    this.sendcommand(comands)

//
//                    this.sendcommand("CLS\r\r\n")
//                    txtLog.append("codepage: " + this.sendcommand("CODEPAGE 1251\r\r\n") + "\r\r\n")
//                    txtLog.append("size: " + this.sendcommand("SIZE 60 mm, 59 mm\r\r\n") + "\r\r\n")
//                    txtLog.append("speed: " + this.sendcommand("SPEED 4\r\r\n")+ "\r\r\n")
//                    this.sendcommand("DENSITY 12\r\r\n")
//                    this.sendcommand("FONTMAIN\$=\"TNR13\"\r\r\n")
//                    txtLog.append("textПривет: " + this.sendcommand("TEXT 0,0,FONTMAIN\$,0,1,1,\"Привет\"\r\r\n")+ "\r\r\n")
//                    this.sendcommand("TEXT 0,40,\"2\",0,1,1,\"Привет\"\r\r\n")
//                    "textHello: " + this.sendcommand("TEXT 0,80,FONTMAIN\$,0,1,1,\"Hello\"\r\r\n")+ "\r\r\n")
//
//                    this.sendcommand("TEXT 0,120,\"2\",0,1,1,\"Hello\"\r\r\n")
//                    this.sendcommand("PRINT 1\r\r\n")
//                    this.sendcommand("END\r\r\n")

                    this.closeport(3000)
                }
            })
        }
    }
}
