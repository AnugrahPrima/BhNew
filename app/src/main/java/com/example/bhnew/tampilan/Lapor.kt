package com.example.bhnew

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

data class Report(
    val description: String,
    val imageUri: String
)
val popin = FontFamily(
    Font(R.font.popinregular, FontWeight.Normal),
    Font(R.font.popinlight, FontWeight.Light),
    Font(R.font.popinbold, FontWeight.Bold),
    Font(R.font.popinsemi, FontWeight.SemiBold)
)



@Composable
fun InputScreen(navController: NavHostController) {
    var description by remember { mutableStateOf("") }
    var hasilfoto = remember { mutableStateOf<Bitmap?>(null) }
    var point by remember { mutableStateOf<Long?>(0)  }
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    val uid = sharedPreferences.getString("UID", "")

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()) {
            bitmap -> if (bitmap!= null){
        hasilfoto.value = bitmap
    }
    }

    LaunchedEffect(Unit) {
        val firebase = FirebaseFirestore.getInstance()
        val config = firebase.collection("users")

        config.whereEqualTo("uid",uid).get().addOnSuccessListener { result ->
            point = result.documents[0].getLong("point")
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(25.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Ambil Foto dan Masukkan Keterangan", fontSize = 20.sp, fontFamily = popin,
                fontWeight = FontWeight.SemiBold,)
            Spacer(modifier = Modifier.height(16.dp))

            hasilfoto.value?.let { bitmap ->
                Image(
                    painter = BitmapPainter(bitmap.asImageBitmap()),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp).aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
            } ?: run{
                Box(
                    modifier = Modifier.size(300.dp).aspectRatio(1f).background(color = Color.LightGray),
                   contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )

                }
            }



            Button(
                modifier = Modifier.fillMaxWidth(0.5f).padding(top = 10.dp), onClick = {
                    cameraLauncher.launch(null)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                )
            ) {
                Text(text = "Ambil Foto",fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,)
            }


            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Keterangan",fontFamily = popin,
                    fontWeight = FontWeight.Light,) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (uid != null) {
                            TambahData(uid,hasilfoto.value,context,description,point)
                            message = "Foto Berhasil Disimpan"
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Simpan Foto",fontFamily = popin,
                        fontWeight = FontWeight.SemiBold,)
                }

            Text(
                text = message,
                style = TextStyle(
                    color = Color.Green,
                    fontWeight = FontWeight.Bold
                )
            )

        }

        Text(
            text = "Lihat Riwayat",
            fontFamily = popin,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .offset(y = -50.dp)
                .clickable(onClick =  {navController.navigate("hasilriwayat")})
        )
    }
}

@Composable
fun ResultScreen(reports: List<Report>) {
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
                        // Image Section - Membuat gambar melebar
                        Image(
                            painter = rememberAsyncImagePainter(report.imageUri),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth() // Gambar akan mengisi lebar layar
                                .heightIn(min = 200.dp) // Mengatur tinggi minimum gambar
                                .clip(RoundedCornerShape(20.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Description Section
                        Text(
                            text = report.description,
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

fun TambahData(uid: String, bit: Bitmap?, context: Context, Keterangan: String,point : Long? ){
    val randomfile = UUID.randomUUID().toString()
    val filepath = bit?.let { saveImage(context,it,randomfile) }
    val firebase = FirebaseFirestore.getInstance()
    val config = firebase.collection("riwayatfoto")

    val data = hashMapOf(
        "UID" to uid,
        "FOTO" to filepath,
        "KETERANGAN" to Keterangan,
        "TANGGAL" to Timestamp.now()
    )
    config.add(data)

    val baru = point?.plus(5)
    val config2 = firebase.collection("users")
    config2.whereEqualTo("uid",uid).get().addOnSuccessListener { result ->
        for (document in result){
            document.reference.update("point",baru)
        }
    }




}

fun saveImage(context: Context,bitmap: Bitmap,filename : String): String?{
    val file = File(context.filesDir, "$filename.jpg")
    return try {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

