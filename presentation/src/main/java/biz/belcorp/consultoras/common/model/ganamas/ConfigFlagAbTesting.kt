package biz.belcorp.consultoras.common.model.ganamas

class ConfigFlagAbTesting(val tipoOferta: String, val orden: Int) {
}

class ConfigABTestingBonificaciones(var variantea: Boolean, var varianteb: Boolean,var variantec: Boolean)
{
    constructor(): this(false, false, false)
}
