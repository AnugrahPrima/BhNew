package com.example.bhnew.data

import com.google.firebase.Timestamp

data class Riwayat(
    val UID : String = "",
    val PANTAI : String ="",
    val POINT : Int = 0,
    val PLASTIK : Int = 0,
    val KAYU : Int = 0,
    val KAIN : Int = 0,
    val FOTO : String = "",
    val TANGGAL : Timestamp = Timestamp.now()
)
