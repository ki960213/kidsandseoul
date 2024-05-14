package com.ki960213.kidsandseoul.data.firebase

import androidx.core.net.toUri
import dev.gitlive.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageStorage @Inject constructor(private val storage: FirebaseStorage) {

    suspend fun uploadImage(
        imageName: String,
        imageFile: File,
    ): String = withContext(Dispatchers.IO) {
        val reference = storage.reference
        val imageRef = reference.child(PREFIX_IMAGES_PATH + imageName)
        imageRef.putFile(dev.gitlive.firebase.storage.File(imageFile.toUri()))
        imageRef.getDownloadUrl()
    }

    suspend fun uploadImages(imageNameAndFiles: List<Pair<String, File>>): List<String> =
        withContext(Dispatchers.IO) {
            val reference = storage.reference
            imageNameAndFiles.map { (imageName, imageFile) ->
                val imageRef = reference.child(PREFIX_IMAGES_PATH + imageName)
                imageRef.putFile(dev.gitlive.firebase.storage.File(imageFile.toUri()))
                imageRef.android.downloadUrl.asDeferred()
            }.awaitAll()
                .map { it.toString() }
        }

    suspend fun deleteImages(imageNames: List<String>) =
        withContext(Dispatchers.IO) {
            val reference = storage.reference
            imageNames.map { imageName ->
                launch {
                    val imageRef = reference.child(PREFIX_IMAGES_PATH + imageName)
                    runCatching { imageRef.delete() }
                }
            }.joinAll()
        }

    suspend fun deleteImage(imageName: String) = withContext(Dispatchers.IO) {
        val reference = storage.reference
        val imageRef = reference.child(PREFIX_IMAGES_PATH + imageName)
        runCatching { imageRef.delete() }
    }

    companion object {

        private const val PREFIX_IMAGES_PATH = "images/"
    }
}
