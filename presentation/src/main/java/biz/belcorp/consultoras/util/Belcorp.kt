package biz.belcorp.consultoras.util

import android.content.Context
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.util.anotation.BrandCode
import biz.belcorp.consultoras.util.anotation.BrandType
import java.util.*

object Belcorp {

    fun checkBelcorpExperience() : Boolean {

        //Date Now
        val calendar = Calendar.getInstance()

        //Expire : 28 de Setiembre del 2018
        val belcorpCalendar = Calendar.getInstance()
        belcorpCalendar.set(Calendar.YEAR, 2018)
        belcorpCalendar.set(Calendar.MONTH, Calendar.SEPTEMBER)
        belcorpCalendar.set(Calendar.DAY_OF_MONTH, 28)
        belcorpCalendar.set(Calendar.HOUR, 23)
        belcorpCalendar.set(Calendar.MINUTE, 59)

        return (calendar.get(Calendar.YEAR) == belcorpCalendar.get(Calendar.YEAR) && calendar.before(belcorpCalendar))
    }

    fun checkChristmasExperience(): Boolean{

        //Date Now
        val calendar = Calendar.getInstance()

        //Inicio : 15 de Octubre del 2018
        val beginDate = Calendar.getInstance()
        beginDate.set(Calendar.YEAR, 2018)
        beginDate.set(Calendar.MONTH, Calendar.OCTOBER)
        beginDate.set(Calendar.DAY_OF_MONTH, 15)
        beginDate.set(Calendar.HOUR, 0)
        beginDate.set(Calendar.MINUTE, 0)

        //Expire : 7 de Enero del 2019
        val expireDate = Calendar.getInstance()
        expireDate.set(Calendar.YEAR, 2019)
        expireDate.set(Calendar.MONTH, Calendar.JANUARY)
        expireDate.set(Calendar.DAY_OF_MONTH, 7)
        expireDate.set(Calendar.HOUR, 23)
        expireDate.set(Calendar.MINUTE, 59)

        return (calendar.after(beginDate) && calendar.before(expireDate))
    }

    fun getBrand(textBrand: String?, id: Int?): String {
        var brand = ""
        textBrand?.let { brand = textBrand }

        if (brand.isBlank()) brand = getBrandById(id!!)
        if (brand.isBlank()) brand = GlobalConstant.NOT_AVAILABLE

        return brand
    }


    fun getBrandById(id: Int): String {
        var brand = GlobalConstant.NOT_AVAILABLE

        when (id){
            BrandCode.LBEL -> brand = BrandType.LBEL
            BrandCode.ESIKA -> brand = BrandType.ESIKA
            BrandCode.CYZONE -> brand = BrandType.CYZONE
            BrandCode.FINART -> brand = BrandType.FINART
        }

        return brand
    }

    fun getBrandOrigenById(id: Int): String {
        var brand = GlobalConstant.NOT_AVAILABLE

        when (id){
            BrandCode.LBEL -> brand = BrandType.LBEL_ORIGEN
            BrandCode.ESIKA -> brand = BrandType.ESIKA
            BrandCode.CYZONE -> brand = BrandType.CYZONE
            BrandCode.FINART -> brand = BrandType.FINART
        }

        return brand
    }

    fun checkMaxAmmount(ammount: Double) : Boolean {
        return when (ammount){
            0.00 -> true
            99999999.00 -> true
            999999999.00 -> true
            9999999999.00 -> true
            99999999999.00 -> true
            999999999999.00 -> true
            else -> false
        }
    }

    fun isValidCuv(context: Context, cuv: String): Boolean {
        val min = context.resources.getInteger(R.integer.cuv_min_length)
        val max = context.resources.getInteger(R.integer.cuv_max_length)
        val regex = "^\\d{${min},${max}}\$".toRegex()
        return cuv.matches(regex)
    }
}
