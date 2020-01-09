package biz.belcorp.consultoras.util

import biz.belcorp.library.log.BelcorpLogger
import com.instacart.library.truetime.TrueTime
import java.util.*

object TimeUtil {

    fun setServerTime(serverTime: Boolean) : Long {
        return if (serverTime) getCurrentTime() else getDeviceTime()
    }

    /**
     * Obtiene la hora actual en milisegundos teniendo como prioridad la que devulve la libreria TrueTime
     */
    fun getCurrentTime(): Long{
        return getServerTime() ?: getDeviceTime()
    }

    /**
     * Obtiene la hora actual en milisegundos usando la libreria trueTime (time.google.com)
     */
    fun getServerTime(): Long? {
        try {
            if(TrueTime.isInitialized()){ return getCalendar().timeInMillis - TrueTime.now().time }
        }catch (e: Exception){
            BelcorpLogger.w("TimeUtil", e.message)
        }
        return null
    }

    /**
     * Obtiene la hora actual en milisegundos del dispositivoio
     */
    fun getDeviceTime() : Long {
        return getCalendar().timeInMillis - System.currentTimeMillis()
    }

    private fun getCalendar(): Calendar{
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, 1)
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        return c
    }

}
