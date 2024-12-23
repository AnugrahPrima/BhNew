package com.example.bhnew.tampilan

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.bhnew.data.ListFoto
import com.example.bhnew.popin
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject


@Composable
fun HasilRiwayat(){

    val reports = remember { mutableStateListOf<ListFoto>() }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    val uid = sharedPreferences.getString("UID", "")


    LaunchedEffect(Unit) {
        val firebase = FirebaseFirestore.getInstance()
        val config = firebase.collection("riwayatfoto")

        config.whereEqualTo("UID",uid).orderBy("TANGGAL",Query.Direction.DESCENDING).get().addOnSuccessListener { result ->
            for (document in result){
                val user = document.toObject(ListFoto::class.java)
                reports.add(user)
            }

        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(25.dp)) {
        Text(text = "Hasil Laporan", fontSize = 20.sp, fontFamily = popin,
            fontWeight = FontWeight.SemiBold,)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(reports) { report ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        val bitmap = BitmapFactory.decodeFile(report.FOTO)
                        if (bitmap != null){
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth() // Gambar akan mengisi lebar layar
                                    .heightIn(min = 200.dp) // Mengatur tinggi minimum gambar
                                    .clip(RoundedCornerShape(20.dp))
                            )

                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Description Section
                        Text(
                            text = report.KETERANGAN,
                            fontFamily = popin,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}