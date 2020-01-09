package biz.belcorp.consultoras.data.provider

import android.content.*
import android.database.Cursor
import android.net.Uri
import biz.belcorp.consultoras.data.BuildConfig
import biz.belcorp.consultoras.data.db.ConsultorasDatabase
import biz.belcorp.consultoras.data.entity.ClienteEntity
import biz.belcorp.consultoras.data.entity.ProductoMasivoEntity
import biz.belcorp.consultoras.data.entity.UserEntity
import biz.belcorp.consultoras.data.manager.SessionManager
import biz.belcorp.library.log.BelcorpLogger
import com.raizlabs.android.dbflow.annotation.ConflictAction
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.runtime.BaseContentProvider
import java.lang.IllegalArgumentException

class Consultant : BaseContentProvider() {

    companion object {

        const val AUTHORITY = BuildConfig.CONTENT_PROVIDER_AUTHORITY

        private const val USER_ENTITY_CONTENT_URI = 0
        private const val PRODUCT_CONTENT_URI = 1
        private const val CLIENT_ENTITY_CONTENT_URI = 2

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

    }

    override fun onCreate(): Boolean {
        MATCHER.addURI(AUTHORITY, UserEntity.ENDPOINT, USER_ENTITY_CONTENT_URI)
        MATCHER.addURI(AUTHORITY, ProductoMasivoEntity.ENDPOINT, PRODUCT_CONTENT_URI)
        MATCHER.addURI(AUTHORITY, ClienteEntity.ENDPOINT, CLIENT_ENTITY_CONTENT_URI)
        BelcorpLogger.i("ConsultantBaseContentProvider")
        return super.onCreate()
    }

    override fun getDatabaseName(): String {
        return ConsultorasDatabase.NAME
    }

    override fun getType(uri: Uri): String? {
        return when (MATCHER.match(uri)) {
            USER_ENTITY_CONTENT_URI -> {
                "${ContentResolver.CURSOR_DIR_BASE_TYPE}/$AUTHORITY/${UserEntity.ENDPOINT}"
            }
            PRODUCT_CONTENT_URI -> {
                "${ContentResolver.CURSOR_DIR_BASE_TYPE}/$AUTHORITY/${ProductoMasivoEntity.ENDPOINT}"
            }
            CLIENT_ENTITY_CONTENT_URI -> {
                "${ContentResolver.CURSOR_DIR_BASE_TYPE}/$AUTHORITY/${ClienteEntity.ENDPOINT}"
            }
            else -> {
                throw IllegalArgumentException("Unknown URI $uri")
            }
        }
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        var cursor: android.database.Cursor? = null

        context?.let {
            val sessionManager = SessionManager.getInstance(it)

            if (sessionManager.isAuthenticated!!)
                when (MATCHER.match(uri)) {
                    USER_ENTITY_CONTENT_URI -> {
                        cursor = FlowManager.getDatabase(ConsultorasDatabase.NAME)
                            .writableDatabase.query(UserEntity.NAME, projection, selection,
                            selectionArgs, null, null, sortOrder)
                    }
                    PRODUCT_CONTENT_URI -> {
                        cursor = FlowManager.getDatabase(ConsultorasDatabase.NAME)
                            .writableDatabase.query(ProductoMasivoEntity.NAME, projection, selection,
                            selectionArgs, null, null, sortOrder)
                    }
                    CLIENT_ENTITY_CONTENT_URI -> {
                        cursor = FlowManager.getDatabase(ConsultorasDatabase.NAME)
                            .writableDatabase.query(ClienteEntity.NAME, projection, selection,
                            selectionArgs, null, null, sortOrder)
                    }
                }
            cursor?.setNotificationUri(it.contentResolver, uri)
        }

        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        return context?.let {
            val sessionManager = SessionManager.getInstance(it)
            if (sessionManager.isAuthenticated!!)
                when (MATCHER.match(uri)) {
                    USER_ENTITY_CONTENT_URI -> {
                        val adapter = FlowManager.getModelAdapter(FlowManager
                            .getTableClassForName(ConsultorasDatabase.NAME, UserEntity.NAME))
                        val id = FlowManager.getDatabase(ConsultorasDatabase.NAME)
                            .writableDatabase.insertWithOnConflict(UserEntity.NAME, null,
                            values, ConflictAction.getSQLiteDatabaseAlgorithmInt(adapter.insertOnConflictAction))
                        it!!.contentResolver.notifyChange(uri, null)
                        return ContentUris.withAppendedId(uri, id)
                    }
                    CLIENT_ENTITY_CONTENT_URI -> {
                        val adapter = FlowManager.getModelAdapter(FlowManager
                            .getTableClassForName(ConsultorasDatabase.NAME, ClienteEntity.NAME))
                        val id = FlowManager.getDatabase(ConsultorasDatabase.NAME)
                            .writableDatabase.insertWithOnConflict(ClienteEntity.NAME, null,
                            values, ConflictAction.getSQLiteDatabaseAlgorithmInt(adapter.insertOnConflictAction))
                        it!!.contentResolver.notifyChange(uri, null)
                        return ContentUris.withAppendedId(uri, id)
                    }
                    else -> {
                        throw IllegalArgumentException("Unknown URI $uri")
                    }
                }
            else
                throw IllegalArgumentException("Session NO active for URI $uri")
        }

    }

    override fun bulkInsert(uri: Uri, values: ContentValues): Int {

        context?.let{
            val sessionManager = SessionManager.getInstance(it)
            if (sessionManager.isAuthenticated!!)
                when (MATCHER.match(uri)) {
                    USER_ENTITY_CONTENT_URI -> {
                        val adapter = FlowManager.getModelAdapter(FlowManager
                            .getTableClassForName(ConsultorasDatabase.NAME, UserEntity.NAME))
                        val id = FlowManager.getDatabase(ConsultorasDatabase.NAME).writableDatabase
                            .insertWithOnConflict(UserEntity.NAME, null, values,
                                ConflictAction.getSQLiteDatabaseAlgorithmInt(adapter.insertOnConflictAction))
                        it!!.contentResolver.notifyChange(uri, null)
                        return if (id > 0) 1 else 0
                    }
                    CLIENT_ENTITY_CONTENT_URI -> {
                        val adapter = FlowManager.getModelAdapter(FlowManager
                            .getTableClassForName(ConsultorasDatabase.NAME, ClienteEntity.NAME))
                        val id = FlowManager.getDatabase(ConsultorasDatabase.NAME).writableDatabase
                            .insertWithOnConflict(ClienteEntity.NAME, null, values,
                                ConflictAction.getSQLiteDatabaseAlgorithmInt(adapter.insertOnConflictAction))
                        it!!.contentResolver.notifyChange(uri, null)
                        return if (id > 0) 1 else 0
                    }
                    else -> {
                        throw IllegalArgumentException("Unknown URI $uri")
                    }
                }
            else
                throw IllegalArgumentException("Session NO active for URI $uri")
        }?:run{
            throw IllegalArgumentException("Session NO active for URI $uri")
        }

    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {

        context?.let {
            val sessionManager = SessionManager.getInstance(it)
            if (sessionManager.isAuthenticated!!)
                when (MATCHER.match(uri)) {
                    USER_ENTITY_CONTENT_URI -> {
                        val count = FlowManager.getDatabase(ConsultorasDatabase.NAME).writableDatabase
                            .delete(UserEntity.NAME, selection, selectionArgs).toLong()
                        if (count > 0) {
                            it?.contentResolver?.notifyChange(uri, null)
                        }
                        return count.toInt()
                    }
                    CLIENT_ENTITY_CONTENT_URI -> {
                        val count = FlowManager.getDatabase(ConsultorasDatabase.NAME).writableDatabase
                            .delete(ClienteEntity.NAME, selection, selectionArgs).toLong()
                        if (count > 0) {
                            it?.contentResolver?.notifyChange(uri, null)
                        }
                        return count.toInt()
                    }
                    else -> {
                        throw IllegalArgumentException("Unknown URI $uri")
                    }
                }
            else
                throw IllegalArgumentException("Session NO active for URI $uri")
        }?:run{
            throw IllegalArgumentException("Session NO active for URI $uri")
        }

    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {

        context?.let{
            val sessionManager = SessionManager.getInstance(it)
            if (sessionManager.isAuthenticated!!)
                when (MATCHER.match(uri)) {
                    USER_ENTITY_CONTENT_URI -> {
                        val adapter = FlowManager.getModelAdapter(FlowManager
                            .getTableClassForName(ConsultorasDatabase.NAME, UserEntity.NAME))
                        val count = FlowManager.getDatabase(ConsultorasDatabase.NAME).writableDatabase
                            .updateWithOnConflict(UserEntity.NAME, values, selection, selectionArgs,
                                ConflictAction.getSQLiteDatabaseAlgorithmInt(adapter.updateOnConflictAction))
                        if (count > 0) {
                            it!!.contentResolver.notifyChange(uri, null)
                        }
                        return count.toInt()
                    }
                    CLIENT_ENTITY_CONTENT_URI -> {
                        val adapter = FlowManager.getModelAdapter(FlowManager
                            .getTableClassForName(ConsultorasDatabase.NAME, ClienteEntity.NAME))
                        val count = FlowManager.getDatabase(ConsultorasDatabase.NAME).writableDatabase
                            .updateWithOnConflict(ClienteEntity.NAME, values, selection, selectionArgs,
                                ConflictAction.getSQLiteDatabaseAlgorithmInt(adapter.updateOnConflictAction))
                        if (count > 0) {
                            it!!.contentResolver.notifyChange(uri, null)
                        }
                        return count.toInt()
                    }
                    else -> {
                        throw IllegalArgumentException("Unknown URI $uri")
                    }
                }
            else
                throw IllegalArgumentException("Session NO active for URI $uri")
        }?:run{
            throw IllegalArgumentException("Session NO active for URI $uri")
        }
    }
    
}
