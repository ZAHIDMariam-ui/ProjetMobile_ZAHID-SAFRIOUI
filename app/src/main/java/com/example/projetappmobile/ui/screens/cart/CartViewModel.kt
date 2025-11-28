package com.example.projetappmobile.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetappmobile.ui.screens.home.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class CartItem(
    val product: Product,
    var quantity: Int = 1,
    var selectedSize: String = "M"
)

class CartViewModel : ViewModel() {

    companion object {
        val instance = CartViewModel()
    }

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addProduct(product: Product, size: String = "M") {
        val current = _cartItems.value.toMutableList()
        val existing = current.find {
            it.product.id == product.id && it.selectedSize == size
        }

        if (existing != null) {
            existing.quantity += 1
        } else {
            current.add(CartItem(product = product, quantity = 1, selectedSize = size))
        }
        _cartItems.value = current
    }

    fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeItem(cartItem)
            return
        }
        val list = _cartItems.value.toMutableList()
        val index = list.indexOf(cartItem)
        if (index != -1) {
            list[index] = cartItem.copy(quantity = newQuantity)
            _cartItems.value = list
        }
    }

    // NOUVELLE FONCTION : Mise à jour de la taille
    fun updateSize(cartItem: CartItem, newSize: String) {
        val list = _cartItems.value.toMutableList()
        val index = list.indexOf(cartItem)
        if (index != -1) {
            list[index] = cartItem.copy(selectedSize = newSize)
            _cartItems.value = list
        }
    }

    fun removeItem(cartItem: CartItem) {
        _cartItems.value = _cartItems.value.filter { it != cartItem }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    val totalPrice: StateFlow<Int> = cartItems
        .map { items ->
            items.sumOf { it.product.price * it.quantity }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )
}