package biz.belcorp.consultoras.feature.galery

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import biz.belcorp.consultoras.BuildConfig
import biz.belcorp.consultoras.R
import biz.belcorp.consultoras.util.CommunicationUtils
import biz.belcorp.consultoras.util.GlobalConstant
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_detail_gallery_list.*
import permissions.dispatcher.*
import java.io.File

@RuntimePermissions
class GalleryDetailPageFragment : Fragment() {

    companion object {
        const val STORAGE_CODE = 101
        const val DEFAULT_VALUE_DOWNLOAD_ID = 0L
        const val INTENT_TYPE = "image/png"
    }

    private var listener : onSaveImage? = null
    private var fileModel: ListadoImagenModel? = null
    private var downloadID: Long? = null
    private var shouldShareImage = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_detail_gallery_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.registerReceiver(downloadCompleteReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(downloadCompleteReceiver)
    }

    fun setListener(listener : onSaveImage?){
        this.listener = listener
    }

    private fun init() {
        activity?.let { act ->
            arguments?.let {
                fileModel = it.getParcelable<ListadoImagenModel>(GlobalConstant.ITEM_SELECTED_GALLERY)

                fileModel?.let { f ->
                    tvw_image_name.text = f.titulo
                    Glide.with(act).load(f.urlImagenVisualiza).into(ivw)
                }
            }
        }

        btnGuardarImagen.setOnClickListener {
            shouldShareImage = false
            downloadImageWithPermissionCheck()
        }

        btnCompartir.setOnClickListener {
            shouldShareImage = true
            downloadImageWithPermissionCheck()
        }
    }

    private val downloadCompleteReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, DEFAULT_VALUE_DOWNLOAD_ID)

            downloadID?.let {
                if (id == it) {
                    activity?.let { act ->
                        val manager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

                        val query = DownloadManager.Query()
                        query.setFilterById(id);

                        val cursor = manager.query(query)

                        if (cursor.moveToFirst()) {
                            if (cursor.count > 0) {
                                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                    if(shouldShareImage){
                                        shareImage(intent.extras)
                                    }else{
                                        Toast.makeText(act, R.string.gallery_finished_download, Toast.LENGTH_LONG).show()
                                    }
                                } else {
                                    Toast.makeText(act, R.string.gallery_error_download_message, Toast.LENGTH_LONG).show()
                                }
                            }else{
                                Toast.makeText(act, R.string.gallery_error_download_message, Toast.LENGTH_LONG).show()
                            }
                        }else{
                            Toast.makeText(act, R.string.gallery_error_download_message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun downloadImage(){
        fileModel?.let {
            listener?.let {l ->
                if(shouldShareImage) {
                    l.save(GlobalConstant.GALLERY_COMPARTIR_IMAGEN, it.titulo)
                }else{
                    l.save(GlobalConstant.GALLERY_GUARDAR_IMAGEN, it.titulo)
                }
            }

            val rootDestinationPath = Environment.DIRECTORY_DOWNLOADS + File.separatorChar + BuildConfig.FLAVOR

            val request = DownloadManager.Request(Uri.parse(it.urlImagenDescarga.replace("\"", "")))
                .setTitle(it.titulo)
                .setDescription(it.titulo)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(rootDestinationPath, it.nombreArchivo.replace(" ", "_"))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            if (Build.VERSION.SDK_INT >= 24) {
                request.setRequiresCharging(false)
            }

            activity?.let { act ->
                val downloadManager = act.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadID = downloadManager.enqueue(request)
                Toast.makeText(act, R.string.gallery_started_download, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showRationaleForStorage(request: PermissionRequest) {
        context?.let {
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_write_rationale)
                .setPositiveButton(R.string.button_aceptar) { _, _ -> request.proceed() }
                .setNegativeButton(R.string.button_cancelar) { _, _ -> request.cancel() }
                .setCancelable(false)
                .show()
        }
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onStorageDenied() {
        // Empty
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onStorageNeverAskAgain() {
        context?.let {
            Toast.makeText(it, R.string.permission_write_neverask, Toast.LENGTH_SHORT).show()
            AlertDialog.Builder(it)
                .setMessage(R.string.permission_write_denied)
                .setPositiveButton(R.string.button_go_to_settings) { _, _ ->
                    CommunicationUtils.goToSettingsForResult(this, STORAGE_CODE)
                }
                .setNegativeButton(R.string.button_cancelar) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            STORAGE_CODE -> {
                context?.let {
                    if (ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        downloadImage()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun shareImage(extras : Bundle?){
        extras?.let { ext ->
            val q = DownloadManager.Query()
            val longId = ext.getLong(DownloadManager.EXTRA_DOWNLOAD_ID)
            q.setFilterById(longId)

            context?.let{ ctx ->
                val c : Cursor = (ctx.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).query(q)

                if(c.moveToFirst()){
                    val status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))

                    if(status == DownloadManager.STATUS_SUCCESSFUL){
                        val filePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)).replace("file://","")
                        c.close()

                        val validator = File(filePath)

                        if(validator.exists()){
                            activity?.let{act ->
                                val uri = FileProvider.getUriForFile(act, "${BuildConfig.APPLICATION_ID}.provider.file", validator)

                                val shareIntent = ShareCompat.IntentBuilder.from(act)
                                    .setType(INTENT_TYPE)
                                    .setStream(uri)
                                    .getIntent()

                                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                act.startActivity(Intent.createChooser(shareIntent, resources.getString(R.string.compartir)))
                            }
                        }
                    }
                }
            }
        }
    }

    interface onSaveImage{
        fun save(action : String, imageName : String)
    }
}
