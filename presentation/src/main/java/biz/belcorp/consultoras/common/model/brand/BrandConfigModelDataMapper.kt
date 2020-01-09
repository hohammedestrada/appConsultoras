package biz.belcorp.consultoras.common.model.brand

import biz.belcorp.consultoras.di.PerActivity
import biz.belcorp.consultoras.domain.entity.ContenidoResumen
import biz.belcorp.consultoras.util.GlobalConstant
import javax.inject.Inject

@PerActivity
class BrandConfigModelDataMapper {
    @Inject
    internal constructor() {}

    fun transform(input:ContenidoResumen) : BrandConfigModel{
        return BrandConfigModel().apply {
            input.contenidoDetalle?.forEach { item ->

                when (item.codigoDetalleResumen){
                    GlobalConstant.TAG_BRANDING_CONFIG_HOME ->{
                        imageHomeUrl = item.urlDetalleResumen
                        colorHome = item.accion
                    }

                    GlobalConstant.TAG_BRANDING_CONFIG_ORDER ->{
                        imageOrderUrl = item.urlDetalleResumen
                        colorOrder = item.accion
                    }

                    GlobalConstant.TAG_BRANDING_CONFIG_HEADER_HOME->{
                        imageHomeHeader = item.urlDetalleResumen
                    }

                    GlobalConstant.TAG_BRANDING_CONFIG_ORDER_BAR->{
                        colorOrderBar = item.accion
                    }
                    GlobalConstant.TAG_BRANDING_CONFIG_ORDER_COLOR_TEXT->{
                        colorTextBar = item.accion
                    }
                    else -> {}
                }


            }
        }
    }
}
