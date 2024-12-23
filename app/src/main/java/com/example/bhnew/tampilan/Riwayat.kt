package com.example.bhnew.tampilan

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.D
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.bhnew.R
import com.example.bhnew.data.Riwayat
import com.example.bhnew.popin
import com.google.android.play.core.integrity.d
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun RiwayatContent(navController: NavController) {

    val datariwayat = remember { mutableStateListOf<Riwayat>() }


    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    val uid = sharedPreferences.getString("UID", "")
    val popin = FontFamily(
        Font(R.font.popinregular, FontWeight.Normal),
        Font(R.font.popinlight, FontWeight.Light),
        Font(R.font.popinbold, FontWeight.Bold),
        Font(R.font.popinsemi, FontWeight.SemiBold)
    )

    LaunchedEffect(Unit) {
        val firestore = FirebaseFirestore.getInstance()
        val config = firestore.collection("riwayat")

        config.whereEqualTo("UID",uid).orderBy("TANGGAL",Query.Direction.DESCENDING).get().addOnSuccessListener { result ->
            for (document in result){
                val datalist = document.toObject(Riwayat::class.java)
                datariwayat.add(datalist)
                Log.d("test", datariwayat.toString())
            }
        }


    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF3705FF))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "RIWAYAT",
                color = Color.White,
                fontFamily = popin,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(top = 15.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar
            var searchQuery by remember { mutableStateOf("") }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_search),
                    contentDescription = "Search",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Cari Riwayat",
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    },
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_sort_by_size),
                    contentDescription = "Filter",
                    tint = Color.Gray
                )
            }
        }

        // Content Section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn() {
                items(datariwayat) { item ->
                    RiwayatItem(item, navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}



@Composable
fun RiwayatItem(data: Riwayat, navController: NavController) {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(data.FOTO, "drawable", context.packageName)
    val date = data.TANGGAL.toDate() // Mengonversi Firebase Timestamp ke objek Date
    val format = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable {
                navController.navigate("mskriwayat/${data.PANTAI}/${data.PLASTIK}/${data.KAYU}/${data.KAIN}/${data.FOTO}")
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp).aspectRatio(1f)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Kolom Detail
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = data.PANTAI,
                fontFamily = popin,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "${format.format(date)}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Poin
        Text(
            text = "+${data.POINT}",
            color = Color(0xFF3705FF),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text("😊", fontSize = 20.sp)
    }
}

@Composable
fun RiwayatDetail(locationName: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Detail Riwayat untuk $locationName",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Tambahkan lebih banyak informasi detail untuk lokasi ini
        Text(text = "Informasi lebih lanjut tentang $locationName")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRiwayatContent() {
    RiwayatContent(navController = rememberNavController())
}
