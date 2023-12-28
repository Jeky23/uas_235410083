package com.example.projek_uas_235410083.data

/** * Kelas data yang mewakili status UI saat ini dalam hal [kuantitas], [rasa],
 * [DateOptions],pengambilan yang dipilih [tanggal] dan [harga] */
data class OrderUiState(

    /** Jumlah cupcake yang dipilih (1, 10, 20) */
    val quantity: Int = 0,

    /** Rasa cupcakes dalam urutan (seperti “Chocolate”, “Vanilla”, dll..) */
    val flavor: String = "",

    /** Tanggal pengambilan yang dipilih (seperti “Jan 1") */
    val date: String = "",

    /** Total harga untuk pesanan */
    val price: String = "",

    /** Tanggal pengambilan yang tersedia untuk pesanan */
    val pickupOptions: List<String> = listOf()
)