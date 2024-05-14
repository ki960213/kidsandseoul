package com.ki960213.kidsandseoul.data.network.facility.model

import com.ki960213.domain.facility.model.AllFacilityFilterConditions
import com.ki960213.domain.facility.model.ChildCareFacilityFilterConditions
import com.ki960213.domain.facility.model.FacilityFilterConditions
import com.ki960213.domain.facility.model.KidsCafeFilterConditions
import com.ki960213.domain.facility.model.OtherFacilityFilterConditions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.DayOfWeek

@Serializable
sealed interface NetworkFacilityFilterConditions {

    val ids: Set<Long>
    val name: String?
    val age: Int?
    val boroughIds: Set<Long>
    val administrativeDongIds: Set<Long>
    val area: NetworkArea?

    companion object {

        fun from(
            filterConditions: FacilityFilterConditions,
        ): NetworkFacilityFilterConditions = when (filterConditions) {
            is ChildCareFacilityFilterConditions -> NetworkChildCareFilterConditions(filterConditions)
            is KidsCafeFilterConditions -> NetworkKidsCafeFilterConditions(filterConditions)
            is OtherFacilityFilterConditions -> NetworkOtherFilterConditions(filterConditions)
            is AllFacilityFilterConditions -> NetworkAllFilterConditions(filterConditions)
        }
    }
}

@Serializable
@SerialName("NetworkChildCareFilterConditions")
data class NetworkChildCareFilterConditions(
    override val ids: Set<Long> = emptySet(),
    override val name: String? = null,
    override val age: Int? = null,
    override val boroughIds: Set<Long> = emptySet(),
    override val administrativeDongIds: Set<Long> = emptySet(),
    override val area: NetworkArea? = null,
    val childCareFacilityType: NetworkChildCareFacilityType? = null,
    val mustSaturdayOperate: Boolean = false,
) : NetworkFacilityFilterConditions {

    constructor(conditions: ChildCareFacilityFilterConditions) : this(
        ids = conditions.ids,
        name = conditions.name,
        age = conditions.age,
        boroughIds = conditions.boroughIds,
        administrativeDongIds = conditions.administrativeDongIds,
        area = conditions.area?.let(::NetworkArea),
        childCareFacilityType = conditions.childCareFacilityType?.let { NetworkChildCareFacilityType.from(it) },
        mustSaturdayOperate = conditions.mustSaturdayOperate
    )
}

@Serializable
@SerialName("NetworkKidsCafeFilterConditions")
data class NetworkKidsCafeFilterConditions(
    override val ids: Set<Long> = emptySet(),
    override val name: String? = null,
    override val age: Int? = null,
    override val boroughIds: Set<Long> = emptySet(),
    override val administrativeDongIds: Set<Long> = emptySet(),
    override val area: NetworkArea? = null,
    val daysOfWeek: Set<String> = emptySet(),
) : NetworkFacilityFilterConditions {

    constructor(conditions: KidsCafeFilterConditions) : this(
        ids = conditions.ids,
        name = conditions.name,
        age = conditions.age,
        boroughIds = conditions.boroughIds,
        administrativeDongIds = conditions.administrativeDongIds,
        area = conditions.area?.let(::NetworkArea),
        daysOfWeek = conditions.daysOfWeek.map(DayOfWeek::toString).toSet()
    )
}

@Serializable
@SerialName("NetworkOtherFilterConditions")
data class NetworkOtherFilterConditions(
    override val ids: Set<Long> = emptySet(),
    override val name: String? = null,
    override val age: Int? = null,
    override val boroughIds: Set<Long> = emptySet(),
    override val administrativeDongIds: Set<Long> = emptySet(),
    override val area: NetworkArea? = null,
    val otherFacilityType: NetworkOtherFacilityType? = null,
) : NetworkFacilityFilterConditions {

    constructor(conditions: OtherFacilityFilterConditions) : this(
        ids = conditions.ids,
        name = conditions.name,
        age = conditions.age,
        boroughIds = conditions.boroughIds,
        administrativeDongIds = conditions.administrativeDongIds,
        area = conditions.area?.let(::NetworkArea),
        otherFacilityType = conditions.otherFacilityType?.let { NetworkOtherFacilityType.from(it) },
    )
}

@Serializable
@SerialName("NetworkAllFilterConditions")
data class NetworkAllFilterConditions(
    override val ids: Set<Long> = emptySet(),
    override val name: String? = null,
    override val age: Int? = null,
    override val boroughIds: Set<Long> = emptySet(),
    override val administrativeDongIds: Set<Long> = emptySet(),
    override val area: NetworkArea? = null,
) : NetworkFacilityFilterConditions {

    constructor(conditions: AllFacilityFilterConditions) : this(
        ids = conditions.ids,
        name = conditions.name,
        age = conditions.age,
        boroughIds = conditions.boroughIds,
        administrativeDongIds = conditions.administrativeDongIds,
        area = conditions.area?.let(::NetworkArea),
    )
}
