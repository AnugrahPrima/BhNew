package com.example.bhnew.tampilan

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.bhnew.R
import com.example.bhnew.data.Beach
import com.example.bhnew.popin
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject


@Composable
fun SekitarScreen(navController: NavController) {

    val pantai = remember {mutableStateListOf<Beach>()}
    val context = LocalContext.current

    val firebase = FirebaseFirestore.getInstance()
    val config = firebase.collection("PANTAI")
    val popin = FontFamily(
        Font(R.font.popinregular, FontWeight.Normal),
        Font(R.font.popinlight, FontWeight.Light),
        Font(R.font.popinbold, FontWeight.Bold),
        Font(R.font.popinsemi, FontWeight.SemiBold)
    )
    
    LaunchedEffect(Unit) {
        config.get().addOnSuccessListener { result ->
            for (document in result){
                val user = document.toObject(Beach::class.java)
                pantai.add(user)
            }
        }
    }





    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable._pantai),
            contentDescription = "Background Pantai",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.7f))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PANTAI",
                    fontSize = 20.sp,
                    fontFamily = popin,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(top = 15.dp)
                )
            }

            // Search Bar
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .height(40.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.iconsearch),
                        contentDescription = "Search Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 16.sp),
                        decorationBox = { innerTextField ->
                            if (searchText.text.isEmpty()) {
                                Text("Cari Pantai",fontFamily = popin,
                                    fontWeight = FontWeight.Light, color = Color.Gray)
                            }
                            innerTextField()
                        }
                    )
                }
            }

            // List Pantai
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                items(pantai) { beach ->
                    BeachItem(beach,context,navController)
                }
            }
        }
    }
}

@Composable
fun BeachItem(beach: Beach,context : Context,navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    val resId = context.resources.getIdentifier(beach.FOTO, "drawable", context.packageName)

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = beach.name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Informasi Pantai
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = beach.name,
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = beach.location,
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Text(
                    text = beach.distance,
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = beach.status,
                    color = Color.Blue,
                    fontSize = 12.sp,
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // "Lihat Selengkapnya"
            Text(
                text = "Lihat Selengkapnya>",
                fontFamily = popin,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color(0xFFB0BEC5),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { showDialog = true }
            )
        }
    }

    // Dialog yang ditampilkan saat "Lihat Selengkapnya" diklik
    if (showDialog) {
        DetailDialog(beach = beach,navController , onDismiss = { showDialog = false })
    }
}

@Composable
fun DetailDialog(beach: Beach,navController: NavController, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Gambar Pantai Besar
                Image(
                    painter = painterResource(R.drawable._pantai),
                    contentDescription = "Detail Pantai",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Nama Pantai
                Text(
                    text = beach.name,
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Lokasi dan Jarak
                Text(
                    text = "${beach.location} • ${beach.distance}",
                    fontSize = 14.sp,
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Status Pantai
                Text(
                    text = beach.status,
                    color = Color.Blue,
                    fontSize = 14.sp,
                    fontFamily = popin,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))



                Spacer(modifier = Modifier.height(16.dp))

                // Tombol "Bersihkan"
                Button(
                    onClick = {navController.navigate("bersihkan/${beach.name}/${beach.FOTO}")},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
                ) {
                    Text("Bersihkan",fontFamily = popin,
                        fontWeight = FontWeight.SemiBold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

