package ru.main

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

data class TaskInfo(val start: Long, val end: Long)

object App {
    lateinit var mSecondThread: AffableThread

    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        print("Число потоков n = ")
        val n = readLine()?.toLong()!!;
        //val n = 3;
        val threads:LinkedList<Thread> = LinkedList()
        val end:Long = 50000000
        var part = end/n;
        for(i in 0 until n - 1){
            var taskInfo = TaskInfo(i*part,i*part+part-1)
            threads.add(AffableThread(taskInfo))
        }
        var taskInfo = TaskInfo((n-1)*part,end)
        threads.add(AffableThread(taskInfo))
        val dateStart = Date();
        threads.forEach {it.start()}
        threads.forEach{it.join()}
        val miliseconds = Date().time-dateStart.time


        println(miliseconds)
    }

    class AffableThread(taskInfo: TaskInfo) : Thread() {
        private val taskInfo = taskInfo
        override fun run()    //Этот метод будет выполнен в побочном потоке
        {
            val md5 = MessageDigest.getInstance("SHA-512")
            var str="";
            for (i in taskInfo.start..taskInfo.end){
                var bytes = md5.digest(i.toString().toByteArray(StandardCharsets.UTF_8))
                str = bytes.toString();
            }
            println("""${str}""" );
        }
    }
}
