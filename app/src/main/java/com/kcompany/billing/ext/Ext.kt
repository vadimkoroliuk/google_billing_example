package com.kcompany.billing.ext

import android.app.ActivityManager
import android.app.Application
import android.os.Process
import androidx.core.content.ContextCompat

// your package name is the same with your main process name
fun Application.isMainProcess(): Boolean {
    return packageName == getProcessName()
}

// you can use this method to get current process name, you will get
// name like "com.package.name"(main process name) or "com.package.name:remote"
private fun Application.getProcessName(): String? {
    val processInfos = ContextCompat
        .getSystemService(this, ActivityManager::class.java)
        ?.runningAppProcesses
        ?: listOf()
    val mypid = Process.myPid()
    return processInfos.firstOrNull {
        it?.pid == mypid
    }?.processName
}