package com.ki960213.domain.kid.model

class Kids(
    val parentId: String,
    kids: List<Kid>,
) {

    val kids = kids.sortedByDescending { it.age }

    override fun equals(other: Any?): Boolean {
        if (other !is Kids) return false
        return parentId == other.parentId && kids == other.kids
    }

    override fun hashCode(): Int {
        var result = parentId.hashCode()
        result = result * 31 + kids.hashCode()
        return result
    }
}
