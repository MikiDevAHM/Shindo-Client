package me.miki.shindoapi

import me.miki.shindo.Shindo
import java.io.File

class ShindoAPI {

    private var launchTime: Long = 0
    private var firstLoginFile: File? = null

    fun ShindoAPI() {
        val fm = Shindo.getInstance().fileManager

        firstLoginFile = File(fm.cacheDir, "first.tmp")
    }

    fun init() {
        launchTime = System.currentTimeMillis()
    }

    fun isSpecialUser(): Boolean {
        return true
    }

    fun getLaunchTime(): Long {
        return launchTime
    }

    fun createFirstLoginFile() {
        Shindo.getInstance().fileManager.createFile(firstLoginFile)
    }

    fun isFirstLogin(): Boolean {
        return !firstLoginFile!!.exists()
    }

}