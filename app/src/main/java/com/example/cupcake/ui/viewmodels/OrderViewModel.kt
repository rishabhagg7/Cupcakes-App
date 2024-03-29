package com.example.cupcake.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.cupcake.ui.uistatemodels.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00


class OrderViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState = _uiState.asStateFlow()

    init {
        resetOrder()
    }

    fun setFlavor(
        desiredFlavor: String
    ) {
        _uiState.update { currentState->
            currentState.copy(
                flavor = desiredFlavor
            )
        }
    }

    fun setPickupDate(desiredPickupDate: String) {
        _uiState.update { currentState->
            currentState.copy(
                date = desiredPickupDate,
                price = calculatePrice(pickupDate = desiredPickupDate)
            )}
    }

    fun setQuantity(
        quantity: Int
    ) {
        _uiState.update { currentState->
            currentState.copy(
                quantity = quantity,
                price = calculatePrice(quantity)
            )
        }
    }

    fun resetOrder() {
        _uiState.value = OrderUiState(
            quantity = 0,
            flavor = "",
            date = "",
            price = calculatePrice(),
            pickupOptions = pickUpOption()
        )
    }

    private fun pickUpOption(): List<String> {
        val dateOption = mutableListOf<String>()
        val formatter = SimpleDateFormat("E, MMM d", Locale.getDefault())
        val calender = Calendar.getInstance()
        repeat(4){
            dateOption.add(formatter.format(calender.time))
            calender.add(Calendar.DATE, 1)
        }
        return dateOption
    }

    private fun calculatePrice(
        quantity: Int = _uiState.value.quantity,
        pickupDate: String = _uiState.value.date
    ): String {
        var calculatedPrice = quantity * PRICE_PER_CUPCAKE
        if (pickupDate == pickUpOption()[0]) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        return NumberFormat.getCurrencyInstance().format(calculatedPrice)
    }
}