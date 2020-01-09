package biz.belcorp.consultoras.util

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.ConsultorasApp
import biz.belcorp.consultoras.common.model.kinesis.KinesisModel
import biz.belcorp.consultoras.domain.entity.Login
import biz.belcorp.consultoras.util.anotation.NetworkEventType
import biz.belcorp.library.annotation.DatetimeFormat
import biz.belcorp.library.log.BelcorpLogger
import biz.belcorp.library.mobile.analytics.util.Region
import biz.belcorp.library.util.DeviceUtil
import biz.belcorp.library.util.NetworkUtil
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.kinesis.kinesisrecorder.KinesisRecorder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.AnkoLogger
import java.text.SimpleDateFormat
import java.util.*

class KinesisManager private constructor(private val ctx: Context, private val nameView: String?, private val kinesisModel: KinesisModel?)
    : AnkoLogger {

    private var recorder: KinesisRecorder? = null
    private var telephonyManager: TelephonyManager? = null

    init {

        val region = Region.getRegion(kinesisModel?.region ?: BuildConfig.LOG_ACCESS_REGION)
        val provider: AWSCredentialsProvider = CognitoCachingCredentialsProvider(ctx, kinesisModel?.id ?: id, region)
        recorder = KinesisRecorder(ctx.getDir(dir, 0), region, provider)

    }

    companion object INSTANCE {
        const val TAG = "KinesisManager"

        const val id = BuildConfig.LOG_ACCESS_ID
        const val dir = BuildConfig.LOG_ACCESS_DIR
        const val streamName = BuildConfig.LOG_ACCESS_STREAM

        @SuppressLint("StaticFieldLeak")
        private var instance: KinesisManager? = null

        fun create(ctx: Context,nameView: String?, kinesisModel: KinesisModel): KinesisManager = instance
            ?: KinesisManager(ctx, nameView, kinesisModel)
    }

    fun save(login: Login) {
        telephonyManager = ctx.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?

        val countrySim = telephonyManager!!.networkCountryIso.toUpperCase()
        val dateFormat = SimpleDateFormat(DatetimeFormat.ISO_LOCAL_DATE_TIME, Locale.US)
        val manager = ctx.packageManager
        val info = manager.getPackageInfo(ctx.packageName, 0)
        val parts = info.versionName.split("-")
        val version = parts[0]
        val nameApp = "APPCONSULTORAS"

        var datami = false

        if (ConsultorasApp.getInstance().datamiType == NetworkEventType.DATAMI_AVAILABLE)
            datami = true

        //val offline = !NetworkUtil.isThereInternetConnection(ctx)

        val log = "{" +
            "\"fecha\":\"${dateFormat.format(Date())}\"," +
            "\"aplicacion\":\"$nameApp\"," +
            "\"pais_origen\":\"$countrySim\"," +
            "\"pais_ingreso\":\"${login.countryISO}\"," +
            "\"region\":\"${login.regionCode}\"," +
            "\"zona\":\"${login.zoneCode}\"," +
            "\"seccion\":\"${login.codigoSeccion}\"," +
            "\"rol\":\"${GlobalConstant.ROL_CODE}\"," +
            "\"campania\":\"${login.campaing}\"," +
            "\"usuario\":\"${login.consultantCode}\"," +
            "\"opcion_pantalla\":\"${nameView!!}\"," +
            "\"opcion_accion\":\"INGRESAR\"," +
            "\"dispositivo_categoria\":\"MOBILE\"," +
            "\"dispositivo_so\":\"Android\"," +
            "\"dispositivo_id\":\"${DeviceUtil.getId(ctx)}\"," +
            "\"version\":\"$version\"," +
            "\"offline\":\"$datami\"}"

        recorder?.saveRecord(log, kinesisModel?.streams?.get("stream_usability") ?: streamName)

        try {
            GlobalScope.launch {
                try {
                    recorder?.submitAllRecords()
                } catch (e: Exception) {
                    BelcorpLogger.d(TAG, e.message)
                }
            }
        } catch (e: Exception) {
            BelcorpLogger.d(TAG, e.message)
        }
    }
}
