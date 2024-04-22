package com.ki960213.kidsandseoul.data.firebase.kid

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.ki960213.domain.user.model.Sex
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_KIDS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_USERS
import com.ki960213.kidsandseoul.data.firebase.toTimestamp
import com.ki960213.kidsandseoul.data.firebase.user.UserDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class KidFirestore @Inject constructor(
    private val db: FirebaseFirestore,
) {

    fun getSelectedKidId(userId: String): Flow<String> = db.collection(COLLECTION_USERS)
        .document(userId)
        .snapshots()
        .map { it.toObject<UserDocument>()?.selectedKidId ?: "" }
        .flowOn(Dispatchers.IO)

    fun getKids(userId: String): Flow<List<KidDocument>> = db.collection(COLLECTION_USERS)
        .document(userId)
        .collection(COLLECTION_KIDS)
        .snapshots()
        .map { it.toObjects<KidDocument>() }
        .flowOn(Dispatchers.IO)

    suspend fun addKid(
        parentId: String,
        name: String,
        sex: Sex,
        birthDate: LocalDate,
        administrativeDongId: Long,
    ) = withContext(Dispatchers.IO) {
        val kidRef = db.collection(COLLECTION_USERS)
            .document(parentId)
            .collection(COLLECTION_KIDS)
            .document()
        val data = KidDocument(
            id = kidRef.id,
            name = name,
            sex = sex.name,
            administrativeDongId = administrativeDongId,
            birthDate = birthDate.toTimestamp(),
        )
        kidRef.set(data)
    }

    suspend fun deleteKid(parentId: String, kidId: String) = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_USERS)
            .document(parentId)
            .collection(COLLECTION_KIDS)
            .document(kidId)
            .delete()
    }
}
