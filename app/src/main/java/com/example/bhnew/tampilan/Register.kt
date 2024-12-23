package com.example.bhnew.tampilan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.bhnew.navigation.AppNavGraph
import com.example.bhnew.ui.theme.BhNewTheme
import com.example.bhnew.R
import com.example.bhnew.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(navtologin : () -> Unit){

    var username by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var message by remember { mutableStateOf("") }


    val popin = FontFamily(
        Font(R.font.popinregular, FontWeight.Normal),
        Font(R.font.popinlight, FontWeight.Light),
        Font(R.font.popinbold, FontWeight.Bold),
        Font(R.font.popinsemi, FontWeight.SemiBold)
    )


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.iconappbh),
            contentDescription = "ICON HILANG",
            modifier = Modifier.size(280.dp).padding(top = 100.dp).offset(y = (-20.dp))
        )

        Column(
            modifier = Modifier.fillMaxWidth(0.8f).height(400.dp).clip(RoundedCornerShape(15.dp)).background(color = Color.Blue),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Sign Up",
                fontFamily = popin,
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier.padding(15.dp)
            )

            TextField(
                modifier = Modifier.fillMaxWidth(0.8f).clip(
                    RoundedCornerShape(5.dp))
                ,
                value = username,
                onValueChange = {username = it},
                placeholder = { Text(text = "Username",
                    fontFamily = popin,
                    fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                )
            )
            TextField(
                modifier = Modifier.fillMaxWidth(0.8f).padding(top = 5.dp).clip(
                    RoundedCornerShape(5.dp))
                ,
                value = email,
                onValueChange = {email = it},
                placeholder = { Text(text = "Email",
                    fontFamily = popin,
                    fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                )
            )

            TextField(
                modifier = Modifier.fillMaxWidth(0.8f).padding(top = 5.dp).clip(
                    RoundedCornerShape(5.dp))
                ,
                value = password,
                onValueChange = {password = it},
                placeholder = { Text(text = "Password",
                    fontFamily = popin,
                    fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                )
            )

            Button(
                modifier = Modifier.fillMaxWidth(0.8f).height(55.dp).padding(top = 10.dp),
                onClick = { authRegis(username.text,email.text,password.text) {isSuccess ->

                    if (isSuccess){
                        navtologin()
                    }else {
                        message = "Ada Kesalahan Saat Register"
                    }

                } },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green.copy(alpha = 0.9f)
                ),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    text = "Sign Up",
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(0.8f).padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Sudah Punya Akun?",
                    color = Color.White,
                    fontFamily = popin,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
                Text(
                    text = "Login!",
                    color = Color.Green,
                    fontFamily = popin,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable(onClick = { navtologin() })
                )
            }


        }
        Text(text = message, color = Color.Red, fontFamily = popin,fontWeight = FontWeight.Light,
            style = TextStyle(
                fontSize = 12.sp
            ),
            modifier = Modifier.padding(top = 20.dp)
        )


    }

}

fun authRegis(username: String, email: String, pass: String, OnSuccess: (Boolean) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val user = auth.currentUser
            val uid = user?.uid
            val point = 0
            val build = UserProfileChangeRequest.Builder().setDisplayName(username).build()
            user?.updateProfile(build)?.addOnCompleteListener { profileUpdateTask ->
                if (profileUpdateTask.isSuccessful) {
                    val userData = uid?.let { User(username, email, pass, it,point) }
                    user?.let {
                        if (userData != null) {
                            db.collection("users").document(it.uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    OnSuccess(true)
                                }
                                .addOnFailureListener {
                                    OnSuccess(false)
                                }
                        }
                    }
                } else {
                    OnSuccess(false)
                }
            }
        } else {
            OnSuccess(false)
        }
    }
}