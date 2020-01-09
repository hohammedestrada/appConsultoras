package biz.belcorp.consultoras.data.net.service

import biz.belcorp.consultoras.data.entity.ClientMovementEntity
import biz.belcorp.consultoras.data.entity.DebtEntity
import biz.belcorp.consultoras.data.util.Constant
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

/**
 *
 */
interface IDebtService {

    @Headers(Constant.TRANSFORM, Constant.APP_VERSION, Constant.APP_SO)
    @POST(value = "/api/Cliente/{clienteId}/Movimiento")
    fun uploadDebt(@Body debtEntity: DebtEntity,
                   @Path("clienteId") clientID: String?): Observable<ClientMovementEntity?>
}
