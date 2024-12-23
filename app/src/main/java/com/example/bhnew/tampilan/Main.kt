import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.bhnew.R
import com.example.bhnew.component.NavBar
import com.example.bhnew.popin
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun Main(navcontroller: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    val username = sharedPreferences.getString("username", "User")
    val uid = sharedPreferences.getString("UID", "")
    val popin = FontFamily(
        Font(R.font.popinregular, FontWeight.Normal),
        Font(R.font.popinlight, FontWeight.Light),
        Font(R.font.popinbold, FontWeight.Bold),
        Font(R.font.popinsemi, FontWeight.SemiBold)
    )

    var point by remember { mutableStateOf<Long?>(0) }

    LaunchedEffect(Unit) {
        val firebase = FirebaseFirestore.getInstance()
        val config = firebase.collection("users")

        config.whereEqualTo("uid",uid).get().addOnSuccessListener { result ->
            for (document in result){
                point = document.getLong("point")
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3F51B5))



    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(100.dp)
        ) {
            Text(
                text = "Beach Hero",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = popin,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logoprofil),
                            contentDescription = "Avatar",
                            modifier = Modifier.align(Alignment.Center)
                                .size(200.dp)
                        )
                    }

                    Image(painter = painterResource(R.drawable.logout),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp).clickable(onClick = {navcontroller.navigate("login")}))



                }

                Spacer(modifier = Modifier.width(8.dp))

                // Welcome Text
                Text(
                    text = "Selamat Datang\n        $username!",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,


                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().height(40.dp).background(color = Color(0xFF3F51B5)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Text(text = "Point Anda : ${point}",
                color = Color.White,
                fontFamily = popin,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 12.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)) // Background abu-abu
                .padding(16.dp)
                .offset(y = 100.dp)
        ) {


            // Menu Section
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()


            ) {
                MenuItem("Pantai", R.drawable.logopantaimainpage,navcontroller,"pantai")
                MenuItem("Riwayat", R.drawable.logoriwayatmain,navcontroller,"riwayat")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                MenuItem("Lapor", R.drawable.logolapormain,navcontroller,"lapor")
                MenuItem("Leaderboard", R.drawable.icon_led,navcontroller,"leaderboard")
            }
        }

        // Bottom Navigation
        BottomNavigationBar()
    }
}

@Composable
fun MenuItem(title: String, iconId: Int , navcontroller: NavController, tujuan : String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = {navcontroller.navigate(tujuan)})) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Image(
                painter = painterResource(id = iconId),
                contentDescription = title,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, style = MaterialTheme.typography.bodySmall,fontFamily = popin,
            fontWeight = FontWeight.SemiBold,)
    }
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_setting),
            contentDescription = "Settings"
        )
        Icon(
            painter = painterResource(id = R.drawable.icon_home),
            contentDescription = "Home"
        )
        Icon(
            painter = painterResource(id = R.drawable.icon_notif),
            contentDescription = "Notifications"
        )
    }
}



