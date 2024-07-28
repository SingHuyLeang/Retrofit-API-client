package com.app.retrofit.data

import coil.network.HttpException
import com.app.retrofit.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException

class ProductRepositoryImpl(
	val api: API
):ProductRepository {
	override suspend fun getProducts(): Flow<Result<List<Product>>> {
		return flow {
			val productFromAPI = try {
				api.getProducts()
			} catch (e: IOException) {
				e.printStackTrace()
				emit(Result.Error(message = "IOException : Error loading products."))
				return@flow
			} catch (e: HttpException) {
				e.printStackTrace()
				emit(Result.Error(message = "HttpException : Error loading products."))
				return@flow
			} catch (e: Exception) {
				e.printStackTrace()
				emit(Result.Error(message = "Exception : Error loading products."))
				return@flow
			}
			emit(Result.Success(productFromAPI.products))
		}
	}
}