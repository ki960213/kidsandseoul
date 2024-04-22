package com.ki960213.kidsandseoul.data.firebase.facility

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_FACILITIES
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_USERS
import com.ki960213.kidsandseoul.data.firebase.FIRESTORE_MAX_LIMIT
import com.ki960213.kidsandseoul.data.firebase.user.UserDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FacilityFirestore @Inject constructor(
    private val db: FirebaseFirestore,
) {

    suspend fun fetchFacilityDocuments(
        facilityIds: List<Long>,
    ): List<FacilityDocument> = withContext(Dispatchers.IO) {
        facilityIds.chunked(FIRESTORE_MAX_LIMIT)
            .flatMap { ids ->
                db.collection(COLLECTION_FACILITIES)
                    .whereIn(FacilityDocument.FIELD_ID, ids)
                    .get()
                    .await()
                    .toObjects()
            }
    }

    fun getInterestFacilityIds(userId: String): Flow<List<Long>> = db.collection(COLLECTION_USERS)
        .document(userId)
        .snapshots()
        .map { it.toObject<UserDocument>()?.interestFacilityIds ?: emptyList() }
        .flowOn(Dispatchers.IO)

    fun getFacility(
        facilityId: Long,
    ): Flow<FacilityDocument?> = db.collection(COLLECTION_FACILITIES)
        .whereEqualTo(FacilityDocument.FIELD_ID, facilityId)
        .snapshots()
        .map { it.toObjects<FacilityDocument>().firstOrNull() }
        .flowOn(Dispatchers.IO)
}
