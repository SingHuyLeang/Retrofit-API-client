package com.app.retrofit.data.model

data class Product(
	val brand: String,
	val category: String,
	val description: String,
	val discountPercentage: Double,
	val id: Int,
	val image: List<String>,
	val price: Double,
	val rating:Double,
	val stock: Int,
	val thumbnail: String,
	val title: String
)
