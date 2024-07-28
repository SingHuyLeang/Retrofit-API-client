package com.app.retrofit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.*
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.*
import coil.compose.*
import coil.request.*
import coil.size.Size
import com.app.retrofit.data.ProductRepositoryImpl
import com.app.retrofit.data.model.Product
import com.app.retrofit.presentaion.ProductViewModel
import com.app.retrofit.ui.theme.RetrofitTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

	private val viewModel by viewModels<ProductViewModel>(factoryProducer = {
		object : ViewModelProvider.Factory{
			override fun <T: ViewModel> create(modelClass: Class<T>): T{
				return ProductViewModel(ProductRepositoryImpl(RetrofitInstance.api)) as T
			}
		}
	})

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			RetrofitTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					val products = viewModel.product.collectAsState().value
					val context  = LocalContext.current

					LaunchedEffect(key1 = viewModel.showErrorToastChannel) {
						viewModel.showErrorToastChannel.collectLatest {show->
							if (show) {
								Toast.makeText(context,"Error",Toast.LENGTH_LONG).show()
							}
						}
					}

					if (products.isEmpty()){
						Box (
							modifier = Modifier.fillMaxSize(),
							contentAlignment = Alignment.Center,
						) {
							CircularProgressIndicator()
						}
					} else {
						LazyColumn (
							modifier = Modifier.fillMaxSize(),
							horizontalAlignment = Alignment.CenterHorizontally,
							contentPadding = PaddingValues(16.dp),
						){
							items(products.size){index ->
								Product(product = products[index])
								Spacer(modifier = Modifier.height(16.dp))
							}
						}
					}
				}
			}
		}
	}
	@Composable
	private fun Product(product: Product) {
		val imageState = rememberAsyncImagePainter(
			model = ImageRequest.Builder(LocalContext.current)
				.data(product.thumbnail)
				.size(Size.ORIGINAL)
				.build()
		).state

		Column (
			modifier = Modifier
				.fillMaxWidth()
				.height(300.dp)
				.padding(8.dp)
				.clip(RoundedCornerShape(16.dp))
				.background(MaterialTheme.colorScheme.primaryContainer),
		) {
			if (imageState is AsyncImagePainter.State.Error){
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(200.dp),
					contentAlignment = Alignment.Center,
				){
					Text(text = "Error")
				}
			}

			if (imageState is AsyncImagePainter.State.Success) {
				Image(
					modifier = Modifier
						.fillMaxWidth()
						.height(200.dp),
					painter = imageState.painter,
					contentDescription = product.title,
					contentScale = ContentScale.Crop,
				)
				Spacer(modifier = Modifier.height(10.dp))
				Text(
					text = "${product.title} -- Price: ${product.price}",
					modifier = Modifier.padding(6.dp),
					fontSize = 14.sp,
				)
			}
		}
	}
}

