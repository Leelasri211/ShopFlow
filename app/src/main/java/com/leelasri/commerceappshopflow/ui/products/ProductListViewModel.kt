package com.leelasri.commerceappshopflow.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leelasri.commerceappshopflow.domain.model.CartItem
import com.leelasri.commerceappshopflow.domain.model.Product
import com.leelasri.commerceappshopflow.domain.usecase.AddToCartUseCase
import com.leelasri.commerceappshopflow.domain.usecase.GetProductsUseCase
import com.leelasri.commerceappshopflow.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _selectedCategory = MutableStateFlow("All")

    val searchQuery = _searchQuery.asStateFlow()
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> = combine(
        _allProducts, _searchQuery, _selectedCategory
    ) { products, query, category ->
        products
            .filter { if (category == "All") true else it.category == category }
            .filter { it.title.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _allProducts.value = emptyList()
            getProductsUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> _isLoading.value = true
                    is Resource.Success -> {
                        _isLoading.value = false
                        _error.value = null
                        _allProducts.value = resource.data
                        val cats = listOf("All") + resource.data.map { it.category }.distinct()
                        _categories.value = cats
                    }
                    is Resource.Error -> {
                        _isLoading.value = false
                        _error.value = resource.message
                    }
                }
            }
        }
    }
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            addToCartUseCase(
                CartItem(
                    productId = product.id,
                    title = product.title,
                    price = product.price,
                    image = product.image,
                    quantity = 1
                )
            )
            _snackbarMessage.emit("${product.title.take(20)}... added to cart")
        }
    }
}