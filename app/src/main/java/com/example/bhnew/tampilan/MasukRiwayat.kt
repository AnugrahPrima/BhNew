package com.example.bhnew.tampilan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bhnew.R
import com.example.bhnew.popin


@Composable
fun RiwayatScreen(message: String, plastik: Int, kayu: Int, kain: Int,foto : String)
{
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(foto, "drawable", context.packageName)
    var pointplastik = plastik * 3
    var pointkayu = kayu * 2
    var pointkain = kain * 4
    var totalpoint = pointplastik + pointkayu + pointkain
    val popin = FontFamily(
        Font(R.font.popinregular, FontWeight.Normal),
        Font(R.font.popinlight, FontWeight.Light),
        Font(R.font.popinbold, FontWeight.Bold),
        Font(R.font.popinsemi, FontWeight.SemiBold)
    )

    Column(
        modifier = Modifier
            .background(Color(0xFF3705FF))

    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "RIWAYAT",
                style = MaterialTheme.typography.titleLarge.copy(fontFamily = popin,
                    fontWeight = FontWeight.Bold, color = Color.White),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 30.dp, bottom = 5.dp),
                textAlign = TextAlign.Center
            )
        }

        // Search Bar


        // Riwayat Item
        RiwayatItem(
            locationName = message,
            imageRes = resId, // Replace with your actual image resource
            totalPoints = totalpoint,
            plasticCount = plastik,
            plasticPoints = pointplastik,
            woodCount = kayu,
            woodPoints = pointkayu,
            clothCount = kain,
            clothPoints = pointkain
        )
    }
}

@Composable
fun BasicTextField(value: String, onValueChange: (Any?) -> Unit, textStyle: TextStyle, modifier: Modifier) {

}

@Composable
fun RiwayatItem(
    locationName: String,
    imageRes: Int,
    totalPoints: Int,
    plasticCount: Int,
    plasticPoints: Int,
    woodCount: Int,
    woodPoints: Int,
    clothCount: Int,
    clothPoints: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .offset(y = -25.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(end = 5.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = locationName,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .offset(y = -15.dp)
                        .padding(bottom = 4.dp)
                )
                Text(
                    text = "$totalPoints Point",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3705FF) ,
                    modifier = Modifier
                        .offset(y = -25.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Jumlah Sampah Plastik Yang Dikumpulkan: ",fontFamily = popin,
            fontWeight = FontWeight.SemiBold,)
        Text(
            text = "$plasticCount Plastik",
            fontFamily = popin,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF3705FF))

        Text("Total Point Yang Di Dapatkan: ",fontFamily = popin,
            fontWeight = FontWeight.SemiBold,)
           Text( text = "$plasticPoints Point",
               fontFamily = popin,
               fontWeight = FontWeight.SemiBold,
            color = Color(0xFF3705FF))

        Spacer(modifier = Modifier.height(8.dp))

        Text("Jumlah Sampah Kayu Yang Dikumpulkan:",fontFamily = popin,
            fontWeight = FontWeight.SemiBold,)
        Text( text = "$woodCount Kayu",
            fontFamily = popin,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF3705FF))
        Text("Total Point Yang Di Dapatkan:",fontFamily = popin,
            fontWeight = FontWeight.SemiBold,)
        Text( text = "$woodPoints Point",
            fontFamily = popin,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF3705FF))

        Spacer(modifier = Modifier.height(8.dp))

        Text("Jumlah Sampah Kain Yang Dikumpulkan:",fontFamily = popin,
            fontWeight = FontWeight.SemiBold,)
        Text( text = "$clothCount ",
            fontFamily = popin,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF3705FF))
        Text("Total Point Yang Di Dapatkan:",fontFamily = popin,
            fontWeight = FontWeight.SemiBold,)
        Text( text = "$clothPoints Point",
            fontFamily = popin,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF3705FF))
    }
}


