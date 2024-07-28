package com.app.retrofit.presentaion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.retrofit.data.ProductRepository
import com.app.retrofit.data.Result
import com.app.retrofit.data.model.Product
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(
	private val productRepository: ProductRepository
) : ViewModel() {
	private val _products = MutableStateFlow<List<Product>>(emptyList())
	val product = _products.asStateFlow()

	private val _showErrorToastChannel = Channel<Boolean>()
	val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

	init {
		viewModelScope.launch {
			productRepository.getProducts().collectLatest {result->
				when(result){
					is Result.Error -> {
						_showErrorToastChannel.send(true)
					}
					is Result.Success -> {
						result.data?.let {products ->
							_products.update { products }
						}
					}
				}
			}
		}
	}
}