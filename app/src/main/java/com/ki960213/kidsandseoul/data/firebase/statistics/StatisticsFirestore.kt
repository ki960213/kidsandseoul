package com.ki960213.kidsandseoul.data.firebase.statistics

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_FACILITIES
import com.ki960213.kidsandseoul.data.firebase.COLLECTION_STATISTICS
import com.ki960213.kidsandseoul.data.firebase.FIRESTORE_MAX_LIMIT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class StatisticsFirestore @Inject constructor(
    private val db: FirebaseFirestore,
) {

    suspend fun plusHotPoint(facilityId: Long) = withContext(Dispatchers.IO) {
        val facilityStatisticsRef = db.collection(COLLECTION_STATISTICS)
            .document(LocalDate.now().toString())
            .collection(COLLECTION_FACILITIES)
            .document(facilityId.toString())
        db.runTransaction { transaction ->
            val document = transaction.get(facilityStatisticsRef)
                .toObject<FacilityStatisticsDocument>()
                ?: FacilityStatisticsDocument()
            transaction.set(facilityStatisticsRef, document.copy(hotPoint = document.hotPoint + 1))
        }
    }

    suspend fun fetchHotFacilityIds(): List<Long> = withContext(Dispatchers.IO) {
        db.collection(COLLECTION_STATISTICS)
            .document(LocalDate.now().toString())
            .collection(COLLECTION_FACILITIES)
            .orderBy(FacilityStatisticsDocument.FIELD_HOT_POINT, Query.Direction.DESCENDING)
            .limit(FIRESTORE_MAX_LIMIT.toLong())
            .get()
            .await()
            .map { it.id.toLong() }
    }
}
