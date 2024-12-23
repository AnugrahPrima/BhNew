import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bhnew.R
import com.example.bhnew.data.Leader
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.google.firebase.firestore.Query
import com.example.bhnew.popin


@Composable
fun Leaderboard() {

    val listleader = remember { mutableStateListOf<Leader>() }
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
        val config = firestore.collection("users")
        config.orderBy("point", Query.Direction.DESCENDING) .get().addOnSuccessListener { result ->
           val userlist = result.documents.mapIndexed {index,isi ->
               Leader(
                   uid = isi.getString("uid") ?: "Unknown",
                   username = isi.getString("username") ?: "Unknown",
                   point = isi.getLong("point")?.toInt() ?: 0,
                   rank = index +1
               )
           }
            listleader.addAll(userlist)
        }

    }

    val currentuser = listleader.find { it.uid == uid }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.bg_leaderboard),
            contentDescription = "Background Pantai",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Judul Leaderboard
            Text(
                text = "Leaderboard",
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = popin,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Panel putih untuk leaderboard
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 600.dp) // Mengatur tinggi minimal dan maksimal
                    .background(Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {

                LeaderboardContent(listleader)
            }
            Row(
                modifier = Modifier.fillMaxWidth().height(80.dp).clip(RoundedCornerShape(20.dp)).padding(top = 10.dp).background(color = Color.White),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${currentuser?.rank}",fontFamily = popin,
                    fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 25.dp))
                Text(text = "${currentuser?.username}",
                    modifier = Modifier.width(150.dp),
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,)
                Text(text = "${currentuser?.point}",fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(end = 20.dp))

            }
        }
    }
}

@Composable
fun LeaderboardContent(list: List<Leader>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(list) { index, leader ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(
                        color = when (index) {
                            0 -> Color(0xFFFFF176) // Gold
                            1 -> Color(0xFFBDBDBD) // Silver
                            2 -> Color(0xFFFFAB91) // Bronze
                            else -> Color.White
                        },
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Menampilkan medali untuk peringkat 1, 2, dan 3
                if (index <= 2) {
                    val medalResource = when (index) {
                        0 -> R.drawable.nomor1
                        1 -> R.drawable.nomor2
                        2 -> R.drawable.nomor3
                        else -> null
                    }
                    medalResource?.let {
                        Image(
                            painter = painterResource(id = it),
                            contentDescription = "Medal",
                            modifier = Modifier
                                .size(60.dp) // Ukuran ikon medali
                                .padding(end = 8.dp)
                        )
                    }
                }

                // Kolom untuk menampilkan nama dan poin
                Column(modifier = Modifier.weight(1f)) {
                    // Menampilkan nama dengan atau tanpa nomor urut
                    Text(
                        text = if (index < 3) {
                            leader.username// Untuk peringkat 1, 2, dan 3 tanpa nomor urut
                        } else {
                            "${index + 1}. ${leader.username}" // Untuk peringkat lainnya dengan nomor urut
                        },
                        fontFamily = popin,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }

                // Menampilkan poin di sebelah kanan
                Text(
                    text = leader.point.toString(),
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Blue, // Warna poin biru
                    modifier = Modifier.align(Alignment.CenterVertically) // Menjaga posisi poin di tengah vertikal
                )
            }
        }
    }
}
