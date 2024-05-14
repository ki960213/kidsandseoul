package com.ki960213.kidsandseoul.data.pagesource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ki960213.domain.facility.model.Facility
import com.ki960213.domain.facility.model.FacilityFilterConditions
import com.ki960213.domain.facility.model.FacilityOrder
import com.ki960213.domain.facility.repository.FacilityRepository

class FacilitiesPagingSource(
    private val facilityRepository: FacilityRepository,
    private val filterConditions: FacilityFilterConditions,
    private val facilityOrder: FacilityOrder,
) : PagingSource<Int, Facility>() {

    override fun getRefreshKey(state: PagingState<Int, Facility>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Facility> = try {
        // 만약 키가 정의되어 있지 않으면 0번 페이지에서 시작
        val pageNumber = params.key ?: 0
        val facilities = facilityRepository.getFacilities(
            page = pageNumber,
            size = params.loadSize,
            order = facilityOrder,
            conditions = filterConditions,
        )
        LoadResult.Page(
            data = facilities,
            prevKey = if (pageNumber == 0) null else pageNumber - 1,
            nextKey = if (facilities.isEmpty()) null else pageNumber + 1
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}
