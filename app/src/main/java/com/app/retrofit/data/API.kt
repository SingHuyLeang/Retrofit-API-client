package com.app.retrofit.data

import com.app.retrofit.data.model.Products
import retrofit2.http.GET

interface API {
	@GET("products")
	suspend fun getProducts(): Products

	companion object {
		const val BASE_URL = "https://dummyjson.com/"
	}

//	@GET("products/{type}")
//	suspend fun getProducts(
//		@Path("path") type: String,
//		@Query("page") page: Int,
//		@Query("api_key") apiKey: String
//	): Products
}