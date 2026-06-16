package com.germandebustamante.ringtonemanager.utils.ringtone

import android.content.ContentValues
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.germandebustamante.ringtonemanager.core.model.error.ErrorBO
import com.germandebustamante.ringtonemanager.core.model.ringtone.RingtoneBO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

class RingtoneInstaller(private val context: Context) {

    fun canWriteSettings(): Boolean = Settings.System.canWrite(context)

    suspend fun install(ringtone: RingtoneBO): Either<ErrorBO, Unit> =
        withContext(Dispatchers.IO) {
            try {
                val localFile = downloadToCache(ringtone.name, ringtone.fileUrl)
                val mediaUri = insertIntoMediaStore(ringtone.name, localFile)
                RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, mediaUri)
                Unit.right()
            } catch (e: Exception) {
                ErrorBO.Unknown(e.message.orEmpty()).left()
            }
        }

    private fun downloadToCache(name: String, url: String): File {
        val file = File(context.cacheDir, "${name.toSafeFileName()}.mp3")
        URL(url).openStream().use { input ->
            FileOutputStream(file).use { output -> input.copyTo(output) }
        }
        return file
    }

    private fun insertIntoMediaStore(name: String, file: File): Uri {
        val values = buildContentValues(name)
        val collection = ringtoneCollection()

        val uri = context.contentResolver.insert(collection, values)
            ?: throw IOException("Failed to create MediaStore entry for ringtone")

        writeFileToUri(file, uri)
        finalizePendingEntry(uri, values)

        return uri
    }

    private fun buildContentValues(name: String): ContentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "$name.mp3")
        put(MediaStore.MediaColumns.MIME_TYPE, "audio/mpeg")
        put(MediaStore.Audio.AudioColumns.IS_RINGTONE, 1)
        put(MediaStore.Audio.AudioColumns.IS_NOTIFICATION, 0)
        put(MediaStore.Audio.AudioColumns.IS_ALARM, 0)
        put(MediaStore.Audio.AudioColumns.IS_MUSIC, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_RINGTONES)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        } else {
            val dir = File(
                @Suppress("DEPRECATION") Environment.getExternalStorageDirectory(),
                Environment.DIRECTORY_RINGTONES,
            )
            dir.mkdirs()
            @Suppress("DEPRECATION")
            put(MediaStore.MediaColumns.DATA, File(dir, "$name.mp3").absolutePath)
        }
    }

    private fun ringtoneCollection(): Uri =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            @Suppress("DEPRECATION") MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

    private fun writeFileToUri(file: File, uri: Uri) {
        context.contentResolver.openOutputStream(uri)?.use { out ->
            file.inputStream().use { it.copyTo(out) }
        }
    }

    private fun finalizePendingEntry(uri: Uri, values: ContentValues) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.IS_PENDING, 0)
            context.contentResolver.update(uri, values, null, null)
        }
    }

    private fun String.toSafeFileName(): String = replace(Regex("[^a-zA-Z0-9._-]"), "_")
}
