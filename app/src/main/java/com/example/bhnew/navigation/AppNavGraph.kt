package com.example.bhnew.navigation

import Leaderboard
import Main
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.bhnew.InputScreen
import com.example.bhnew.tampilan.Bersihkan
import com.example.bhnew.tampilan.HasilRiwayat
import com.example.bhnew.tampilan.Login
import com.example.bhnew.tampilan.Register
import com.example.bhnew.tampilan.RiwayatContent
import com.example.bhnew.tampilan.RiwayatScreen
import com.example.bhnew.tampilan.SekitarScreen

@Composable
fun AppNavGraph(navcontroller: NavHostController){
        NavHost(
            navController = navcontroller,
            startDestination = "login",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -1000 },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -1000 },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 1000 },
                    animationSpec = tween(300)
                )
            }
        ) {
            composable("register") {
                Register(navtologin = { navcontroller.navigate("login") })
            }
            composable("login") {
                Login(navtoregister = { navcontroller.navigate("register") }, navtomain = {navcontroller.navigate("main")})
            }
            composable("main") {
                Main(navcontroller)
            }
            composable("pantai") {
                SekitarScreen(navcontroller)
            }
            composable("lapor") {
                InputScreen(navcontroller)
            }
            composable("riwayat") {
                RiwayatContent(navcontroller)
            }
            composable(
                route = "mskriwayat/{message}/{plastik}/{kayu}/{kain}/{foto}",
                arguments = listOf(
                    navArgument("message") { type = NavType.StringType },
                    navArgument("plastik") { type = NavType.IntType },
                    navArgument("kayu") { type = NavType.IntType },
                    navArgument("kain") { type = NavType.IntType },
                    navArgument("foto") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val message = backStackEntry.arguments?.getString("message")
                val plastik = backStackEntry.arguments?.getInt("plastik")
                val kayu = backStackEntry.arguments?.getInt("kayu")
                val kain = backStackEntry.arguments?.getInt("kain")
                val foto = backStackEntry.arguments?.getString("foto")
                if (message != null) {
                    if (plastik != null) {
                        if (kayu != null) {
                            if (kain != null) {
                                if (foto != null) {
                                    RiwayatScreen(message = message,plastik = plastik,kayu = kayu,kain = kain,foto = foto)
                                }
                            }
                        }
                    }
                }
            }
            composable(
                route = "bersihkan/{message}/{foto}",
                arguments = listOf(navArgument("message") { type = NavType.StringType },
                                    navArgument("foto") { type = NavType.StringType })

            ) { backStackEntry ->
                val message = backStackEntry.arguments?.getString("message")
                val foto = backStackEntry.arguments?.getString("foto")
                if (message != null) {
                    if (foto != null) {
                        Bersihkan(message = message,foto = foto, navtomain = {navcontroller.navigate("main")})
                    }
                }
            }
            composable("leaderboard"){
                Leaderboard()
            }
            composable("hasilriwayat"){
                HasilRiwayat()
            }
        }
    }

