package com.example.bhnew.tampilan
import android.content.Context
import android.os.Bundle
import android.os.Message
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
import com.example.bhnew.R
import com.example.bhnew.data.Beach
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bersihkan(message: String,foto : String,navtomain : () -> Unit) {

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    val uid = sharedPreferences.getString("UID", "")

    var point by remember { mutableStateOf<Long?>(0)  }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var satu by remember { mutableStateOf(0) }
    var dua by remember { mutableStateOf(0) }
    var tiga by remember { mutableStateOf(0) }

    val popin = FontFamily(
        Font(R.font.popinregular, FontWeight.Normal),
        Font(R.font.popinlight, FontWeight.Light),
        Font(R.font.popinbold, FontWeight.Bold),
        Font(R.font.popinsemi, FontWeight.SemiBold)
    )

    LaunchedEffect(Unit) {
        val firebase = FirebaseFirestore.getInstance()
        val config = firebase.collection("users")

        config.whereEqualTo("uid",uid).get().addOnSuccessListener { result ->
            point = result.documents[0].getLong("point")
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable._pantai),
            contentDescription = "Background Pantai",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.7f))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "BERSIHKAN",
                    fontSize = 20.sp,
                    fontFamily = popin,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(7.dp)
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.82f).height(120.dp).clip(RoundedCornerShape(10.dp)).background(color = Color.White.copy(alpha = 0.9f)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){ Box(
                modifier = Modifier.size(70.dp).offset(x = 10.dp).border(width = 1.dp, color = Color.LightGray,
                    RoundedCornerShape(10.dp)
                ),
            ) {
                Image(
                    painter = painterResource(R.drawable.iconplastik),
                    contentDescription = null,
                    modifier = Modifier.size(55.dp).aspectRatio(1f).align(Alignment.Center)
                )
                Text(
                    text = "Plastik",
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.BottomCenter).offset(y = 24.dp),
                    fontSize = 12.sp,
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold
                )
            }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.LightGray.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Button(
                        onClick = { if (satu > 0) satu-- },
                        modifier = Modifier.size(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.6f)
                        ))
                    {
                        Text(text = "-", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = TextFieldValue(satu.toString()),
                        onValueChange = {
                            val newValue = it.text.toIntOrNull()
                            if (newValue != null) {
                                satu = newValue
                            }
                        },
                        modifier = Modifier.width(80.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { satu++ }, // Increase the number
                        modifier = Modifier.size(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green.copy(alpha = 0.6f)
                        )) {
                        Text(text = "+", fontSize = 30.sp,
                            color = Color.White)
                    }
                }


            }
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.82f).height(120.dp).clip(RoundedCornerShape(10.dp)).background(color = Color.White.copy(alpha = 0.9f)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){  Box(
                modifier = Modifier.size(70.dp).offset(x = 10.dp).border(width = 1.dp, color = Color.LightGray,
                    RoundedCornerShape(10.dp)
                ),
            ) {
                Image(
                    painter = painterResource(R.drawable.iconkayu),
                    contentDescription = null,
                    modifier = Modifier.size(55.dp).aspectRatio(1f).align(Alignment.Center)
                )
                Text(
                    text = "Kayu",
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.BottomCenter).offset(y = 24.dp),
                    fontSize = 12.sp,
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold
                )
            }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.LightGray.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Button(
                        onClick = { if (dua > 0) dua-- },
                        modifier = Modifier.size(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.6f)
                        ))
                    {
                        Text(text = "-", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = TextFieldValue(dua.toString()),
                        onValueChange = {
                            val newValue = it.text.toIntOrNull()
                            if (newValue != null) {
                                dua = newValue
                            }
                        },
                        modifier = Modifier.width(80.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { dua++ }, // Increase the number
                        modifier = Modifier.size(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green.copy(alpha = 0.6f)
                        )) {
                        Text(text = "+", fontSize = 30.sp,
                            color = Color.White)
                    }
                }


            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.82f).height(120.dp).clip(RoundedCornerShape(10.dp)).background(color = Color.White.copy(alpha = 0.9f)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){  Box(
                modifier = Modifier.size(70.dp).offset(x = 10.dp).border(width = 1.dp, color = Color.LightGray,
                    RoundedCornerShape(10.dp)
                ),
            ) {
                Image(
                    painter = painterResource(R.drawable.iconkain),
                    contentDescription = null,
                    modifier = Modifier.size(55.dp).aspectRatio(1f).align(Alignment.Center)
                )
                Text(
                    text = "Kain",
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.BottomCenter).offset(y = 24.dp),
                    fontSize = 12.sp,
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold
                )
            }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.LightGray.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Button(
                        onClick = { if (tiga > 0) tiga-- },
                        modifier = Modifier.size(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red.copy(alpha = 0.6f)
                        ))
                    {
                        Text(text = "-", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = TextFieldValue(tiga.toString()),
                        onValueChange = {
                            val newValue = it.text.toIntOrNull()
                            if (newValue != null) {
                                tiga = newValue
                            }
                        },
                        modifier = Modifier.width(80.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { tiga++ }, // Increase the number
                        modifier = Modifier.size(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green.copy(alpha = 0.6f)
                        )) {
                        Text(text = "+", fontSize = 30.sp,
                            color = Color.White)
                    }
                }


            }


           Button(
               modifier = Modifier.padding(top = 20.dp).height(50.dp).fillMaxWidth(0.8f),
               onClick = { Submit(satu,dua,tiga,uid!!,point!!,message,foto) { isSuccess ->
                   if (isSuccess){
                       navtomain()


                   }
               } },
               colors = ButtonDefaults.buttonColors(
                   containerColor = Color.White
               ),
               shape = RoundedCornerShape(10.dp)
           ) {
               Text(text = "Submit",
                   fontFamily = popin,
                   fontSize = 15.sp,
                   color = Color.Black,
                   fontWeight = FontWeight.SemiBold,
               )
           }




        }
    }
}

fun Submit(satu : Int,dua : Int, tiga : Int,uid : String, point : Long ,pantai : String,foto : String, onResult : (Boolean) -> Unit){
    var baru = satu * 3
    var barudua = dua * 2
    var barutiga = tiga * 4
    var total = baru + barudua + barutiga
    var hasil = point + baru + barudua + barutiga

    var firebase = FirebaseFirestore.getInstance()
    val config = firebase.collection("users")
    val riwayat = firebase.collection("riwayat")

    config.whereEqualTo("uid",uid).get().addOnSuccessListener { result ->
        for (document in result){
            document.reference.update("point",hasil)
        }
    }

    val data = hashMapOf(
        "PANTAI" to pantai,
        "UID" to uid,
        "POINT" to total,
        "PLASTIK" to satu,
        "KAYU" to dua,
        "KAIN" to tiga,
        "FOTO" to foto,
        "TANGGAL" to Timestamp.now()
    )
    riwayat.add(data).addOnSuccessListener {
        onResult(true)
    }
}
