package com.app.retrofit.data

import com.app.retrofit.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
	suspend fun getProducts(): Flow<Result<List<Product>>>
}