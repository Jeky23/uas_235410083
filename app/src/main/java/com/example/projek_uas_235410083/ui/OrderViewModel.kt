package com.example.projek_uas_235410083.ui

import androidx.lifecycle.ViewModel
import com.example.projek_uas_235410083.data.OrderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/** harga untuk kue tunggal */
private const val PRICE_PER_CUPCAKE = 2.00

/** Biaya tambahan untuk pengambilan pesanan di hari yang sama */
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

/** [OrderViewModel] menyimpan informasi tentang pesanan kue dalam hal kuantitas, rasa, dan
 * tanggal pengambilan. Ia juga tahu bagaimana menghitung harga total berdasarkan detail pesanan.*/
class OrderViewModel : ViewModel() {

    /** state kue untuk pesanan*/
    private val _uiState = MutableStateFlow(OrderUiState(pickupOptions = pickupOptions()))
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    /** Tetapkan kuantitas [numberCupcakes] kue untuk status pesanan dan perbarui harga */
    fun setQuantity(numberCupcakes: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                quantity = numberCupcakes,
                price = calculatePrice(quantity = numberCupcakes)
            )
        }
    }

    /**  Atur [DesiredFlavor] kue untuk status pesanan.* Hanya 1 rasa yang dapat dipilih untuk seluruh pesanan. */
    fun setFlavor(desiredFlavor: String) {
        _uiState.update { currentState ->
            currentState.copy(flavor = desiredFlavor)
        }
    }

    /** Atur [PickUpdate] untuk status pesanan dan perbarui harga */
    fun setDate(pickupDate: String) {
        _uiState.update { currentState ->
            currentState.copy(
                date = pickupDate,
                price = calculatePrice(pickupDate = pickupDate)
            )
        }
    }

    /** Untuk reset pesanan */
    fun resetOrder() {
        _uiState.value = OrderUiState(pickupOptions = pickupOptions())
    }

    /** Mengembalikan harga yang dihitung berdasarkan detail pesanan.*/
    private fun calculatePrice(
        quantity: Int = _uiState.value.quantity,
        pickupDate: String = _uiState.value.date
    ): String {
        var calculatedPrice = quantity * PRICE_PER_CUPCAKE
        /** Jika pengguna memilih opsi pertama (hari ini) untuk pengambilan, tambahkan biaya tambahan*/
        if (pickupOptions()[0] == pickupDate) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        val formattedPrice = NumberFormat.getCurrencyInstance().format(calculatedPrice)
        return formattedPrice
    }

    /** Mengembalikan daftar pilihan tanggal dimulai dengan tanggal saat ini dan 3 tanggal berikut */
    private fun pickupOptions(): List<String> {
        val dateOptions = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        // add current date and the following 3 dates.
        repeat(4) {
            dateOptions.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return dateOptions
    }
}
