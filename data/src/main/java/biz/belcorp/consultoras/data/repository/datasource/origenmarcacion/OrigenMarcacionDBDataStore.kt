package biz.belcorp.consultoras.data.repository.datasource.origenmarcacion

import android.content.Context
import biz.belcorp.consultoras.data.entity.*
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.select


class OrigenMarcacionDBDataStore(val context: Context) : OrigenMarcacionDataStore {

    override fun getValor(tipo: String, codigo: String): String? {
        val concatenado = tipo+codigo
        val query = (select from OrigenMarcacionWebLocalEntity::class).queryList()
        val res = query.filter { it1 -> it1.codigo==concatenado }
            ?.firstOrNull()?.valor
        return res
    }

    override fun getValorPalanca(codigo: String): String? {
        val query = (select from PalancaEntity::class).queryList()
        val res = query.filter { it1 -> it1.codigo==codigo }
            ?.firstOrNull()?.valor
        return res
    }

    override fun getValorSubseccion(codigo: String): String? {
        val query = (select from SubseccionEntity::class).queryList()
        val res = query.filter { it1 -> it1.codigo==codigo }
            ?.firstOrNull()?.valor
        return res
    }


}

