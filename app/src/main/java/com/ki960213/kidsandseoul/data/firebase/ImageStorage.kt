package com.ki960213.kidsandseoul.data.firebase

import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageStorage @Inject constructor(private val storage: FirebaseStorage) {

    suspend fun uploadImage(imageName: String, imageFile: File): String {
        val reference = storage.reference
        val imageRef = reference.child("images/${imageName}")
        imageRef.putFile(imageFile.toUri()).await()
        return imageRef.downloadUrl.await().toString()
    }
}
