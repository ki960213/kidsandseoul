package com.ki960213.kidsandseoul.data.firebase.kid

import com.ki960213.domain.user.model.Sex
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_KIDS
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_USERS
import com.ki960213.kidsandseoul.data.firebase.documentsFlow
import com.ki960213.kidsandseoul.data.firebase.toTimestamp
import dev.gitlive.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class KidFirestore @Inject constructor(
    private val db: FirebaseFirestore,
) {

    fun getKids(userId: String): Flow<List<KidDocument>> =
        db.collection(COLLECTION_USERS)
            .document(userId)
            .collection(COLLECTION_KIDS)
            .documentsFlow<KidDocument>()
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
            .document
        val kidDocument = KidDocument(
            id = kidRef.id,
            name = name,
            sex = sex.name,
            administrativeDongId = administrativeDongId,
            birthDate = birthDate.toTimestamp(),
        )
        kidRef.set(kidDocument)
    }

    suspend fun deleteKid(
        parentId: String,
        kidId: String,
    ) = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_USERS)
            .document(parentId)
            .collection(COLLECTION_KIDS)
            .document(kidId)
            .delete()
    }
}
