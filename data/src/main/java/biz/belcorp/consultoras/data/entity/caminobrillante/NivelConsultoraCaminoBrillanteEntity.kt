package biz.belcorp.consultoras.data.entity.caminobrillante

import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import com.google.gson.annotations.SerializedName
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table

@Table(database = ConsultorasDatabase::class, name = "NivelConsultoraCaminoBrillante")
class NivelConsultoraCaminoBrillanteEntity {

    @PrimaryKey(autoincrement = true)
    @Column(name = "id")
    var id: Int? = null

    @Column(name = "PeriodoCae")
    @SerializedName("PeriodoCae")
    var periodoCae: String? = null

    @Column(name = "Campania")
    @SerializedName("Campania")
    var campania: String? = null

    @Column(name = "Nivel")
    @SerializedName("Nivel")
    var nivel: String? = null

    @Column(name = "MontoPedido")
    @SerializedName("MontoPedido")
    var montoPedido: String? = null

    @Column(name = "FechaIngreso")
    @SerializedName("FechaIngreso")
    var fechaIngreso: String? = null

    @Column(name = "KitSolicitado")
    @SerializedName("KitSolicitado")
    var kitSolicitado: String? = null

    @Column(name = "GananciaCampania")
    @SerializedName("GananciaCampania")
    var gananciaCampania: Double? = null

    @Column(name = "GananciaPeriodo")
    @SerializedName("GananciaPeriodo")
    var gananciaPeriodo: Double? = null

    @Column(name = "GananciaAnual")
    @SerializedName("GananciaAnual")
    var gananciaAnual: Double? = null

    @Column(name = "isActual")
    @SerializedName("EsActual")
    var isActual: Boolean = false

    @Column(name = "FlagSeleccionMisGanancias")
    @SerializedName("FlagSeleccionMisGanancias")
    var isFlagSeleccionMisGanancias: Boolean? = false

    @Column(name = "IndCambioNivel")
    @SerializedName("IndCambioNivel")
    var indCambioNivel: String? = null

    @Column(name = "MensajeCambioNivel")
    @SerializedName("MensajeCambioNivel")
    var mensajeCambioNivel: String? = null

}
