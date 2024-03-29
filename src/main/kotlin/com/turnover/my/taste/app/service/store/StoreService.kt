package com.turnover.my.taste.app.service.store

import com.turnover.my.taste.app.domain.store.dto.StoreDTO
import com.turnover.my.taste.app.exception.EntityNotFoundException
import com.turnover.my.taste.app.repository.store.StoreCustomRepository
import com.turnover.my.taste.app.repository.store.StoreRepository
import org.geolatte.geom.builder.DSL.g
import org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84
import org.geolatte.geom.builder.DSL.point
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true, rollbackFor = [Exception::class])
class StoreService(
    val storeRepository: StoreRepository,
    val storeCustomRepository: StoreCustomRepository,
) {

    fun getStorePoints(): List<StoreDTO.StorePoint> {
        return storeCustomRepository.getStorePoints()
    }

    fun getStoreLiteDetailsByStoreId(storeId: Long): StoreDTO.LiteDetails {
        return storeCustomRepository.getStoreLiteDetailsByStoreId(storeId)
            ?: throw EntityNotFoundException("매장", storeId)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun saveStore(request: StoreDTO.Save): Long? {
        val store = request.toEntity(point(WGS84, g(request.lon, request.lat)))

        return storeRepository.save(store).id
    }
}