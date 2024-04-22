package com.ki960213.domain.kid.model

class Kids(
    val parentId: String,
    val selectedKid: Kid? = null,
    kids: List<Kid>,
) {

    val kids = kids.sortedByDescending { it.age }

    override fun equals(other: Any?): Boolean {
        if (other !is Kids) return false
        return parentId == other.parentId && selectedKid == other.selectedKid && kids == other.kids
    }

    override fun hashCode(): Int {
        var result = parentId.hashCode()
        result = result * 31 + selectedKid.hashCode()
        result = result * 31 + kids.hashCode()
        return result
    }
}
