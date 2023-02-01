package com.example.clase3.presentation

import android.Manifest
import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image


import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextIndent
import androidx.navigation.NavController
import com.example.clase3.R

import com.example.clase3.presentation.theme.Clase3Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.*

import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import  androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.navigation.composable
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender

import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState


@Composable
fun ButtonWidget(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ){
        //Button(onClick = onClick, modifier = iconModifier) {
        //    Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
        //}
    }
}

@Composable
fun TextComponent(modifier: Modifier=Modifier, text:String){
    Text(
        modifier=modifier,
        textAlign= TextAlign.Center,
        color= MaterialTheme.colors.primary,
        text=text
    )
}
@Composable
fun CardComponent(
    modifier: Modifier=Modifier, title:String, weatherDes:String, time:String, temp:Double, ){
    AppCard(
        onClick={/*TODO*/ } , appName= {Text("Clima", color=Color.White)},
        time={Text(time, color= if(temp<12) Color.White else Color.Red)},
        title={Text(title, color=Color.Yellow)}){
        val icon = if(temp<12)   R.mipmap.high   else R.mipmap.low
        Row(horizontalArrangement = Arrangement.Center){
            Image(modifier = Modifier.height(25.dp),
            painter = painterResource(id = icon),
            contentDescription = "icon")
            Spacer(modifier = Modifier.size(7.dp))
           Text(weatherDes)
        }
    }
}

@OptIn(ExperimentalPermissionsApi ::class)
@Composable
fun WearApp(locationUtil: LocationManager){

    var listState= rememberScalingLazyListState()
    Clase3Theme{
    val locationPermissionState=
    rememberMultiplePermissionsState(
        listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    )
    )
        val contentModifier= Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
        val iconModifier= Modifier
            .size(24.dp)
            .padding()
            .wrapContentSize(align = Alignment.Center)

        ScalingLazyColumn(modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 15.dp,
            start = 8.dp,
            end = 8.dp,
           /* bottom = 32.dp*/),
            verticalArrangement = Arrangement.Bottom,
            state = listState,
            autoCentering = AutoCenteringParams(0)
            ){
            item { Spacer(modifier = Modifier.size(20.dp))}
            if (locationPermissionState.allPermissionsGranted){
                if(locationUtil.dataLoaded.value){
                    item { TextComponent(contentModifier, "Acceso a la ubicacion concedido") }
                }
                else{
                    item {
                        CardComponent(
                           modifier = contentModifier,
                            title = locationUtil.data.value.name,
                            weatherDes =locationUtil.data.value.weatherInfo ,
                            time = locationUtil.data.value.time,
                            temp = locationUtil.data.value.temp,
                        )
                    }
                    item{ Spacer(modifier = Modifier.size(20.dp))
                    }
                    /*item {         Button(onClick = { navController.navigate(NavRoute.KEYBOARDSCREEN){
                        popUpTo("MainActivity") {inclusive= true}

                    } }) {
                        Text(text = "Hola")
                    }}*/



                }
            }
            else{
                val allpermission= locationPermissionState.permissions.size== locationPermissionState.revokedPermissions.size
                val textToShow = if(!allpermission){
                    "Concede los permisos porfi"
                }else if(locationPermissionState.shouldShowRationale) {
                    "La ubicacion exacta es necesaria"
                }
else{
        "Para el correcto funcionamiento de la app concede los permisos"
}
                item { TextComponent(contentModifier, textToShow)}
                item { ButtonWidget(contentModifier, iconModifier) {
                    locationPermissionState.launchMultiplePermissionRequest()
                } }
            }
        }
}
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SearchScreen(navController: NavController,locationUtil: LocationManager) {
    val navControllerHorizontal = rememberSwipeDismissableNavController()
    val pagerState = rememberPagerState(
        initialPage = 0
    )
    Scaffold(modifier = Modifier.fillMaxSize()) {


        SwipeDismissableNavHost(
            navController = navControllerHorizontal,
            modifier = Modifier.fillMaxSize(),
            startDestination = "second"
        ) {

            composable(route = "start") {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = { navControllerHorizontal.navigate("second") }) {
                        Text(text = "Comenzar", color = Color.Red)
                    }
                }
            }
            composable(route = "second") {
                val state = rememberPagerState()
                val shape = if (LocalConfiguration.current.isScreenRound) CircleShape else null
                Box(Modifier.fillMaxSize()) {
                    val paperState = rememberPagerState()
                    HorizontalPager(
                        count = 4,
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        if (page == 0) {
                            TextInputs("Latitud")
                        }
                        if (page == 1) {
                            TextInputs("Longitud")
                        }
                        if (page == 2) {
                            Button(onClick = { /*TODO*/ }) {
                                Text(text = "Buscar")
                            }
                        }
                        if (page == 3) {
                            Button(onClick = { navController.navigate(NavRoute.HOMESCREEN) }) {
                                Text(text = "Atras")
                            }
                        }

                    }

                    HorizontalPagerIndicator(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(10.dp),
                        pagerState = pagerState,
                        activeColor = Color.White,
                        inactiveColor = Color.Gray,
                        indicatorHeight = 5.dp,
                        indicatorWidth = 6.dp,

                        )
                }
            }
        }
    }
}
    @Composable
    fun HomeScreen(navController: NavController, locationUtil: LocationManager) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(onClick = { /*TODO*/ }) {
                Button(onClick = { navController.navigate(NavRoute.SEARCHSCREEN) }) {
                    Text(text = "Manual")
                }
                Spacer(modifier = Modifier.size(10.dp))
                Button(onClick = { navController.navigate(NavRoute.AUTOMATICO) }) {
                    Text(text = "Automatico")
                }
            }
        }
    }



@Composable
fun KeyBoard(navegacion: NavController){
TextInputs("Hola")
}
object NavRoute {

    const val HOMESCREEN = "HomeScreen"
    const val  SEARCHSCREEN= "SearchScreen/{id}"
    const val  KEYBOARDSCREEN= "keyboardScreen"
    const val MAINACTIVITY ="MainActivity"
    const val AUTOMATICO="Automatico"
}
@Composable
fun TextInputs(key:String) {
    val label = remember { mutableStateOf(key) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data ->
                val results: Bundle = RemoteInput.getResultsFromIntent(data)
                val ipAddress: CharSequence? = results.getCharSequence("ip_address")
               val ip= ipAddress.toString()
                Log.i("TAG", "TextInputs: $ip")
                label.value = ipAddress.toString()
            }
        }
    Scaffold() {
        Column() {
            Spacer(modifier = Modifier.height(20.dp))
            Chip(
                label = { Text(label.value) },
                onClick = {}
            )
            Chip(
                label = { Text("Buscar $key") },
                onClick = {
                    val intent: Intent = RemoteInputIntentHelper.createActionRemoteInputIntent();
                    val remoteInputs: List<RemoteInput> = listOf(
                        RemoteInput.Builder("ip_address")
                            .setLabel("Entrada de $key manual")
                            .wearableExtender {
                                setEmojisAllowed(false)
                                setInputActionType(EditorInfo.IME_ACTION_DONE)
                            }.build()
                    )

                    RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)

                    launcher.launch(intent)
                }
            )
        }
    }

}


@Composable
fun prueba(  locationUtil: LocationManager = LocationManager()){

    val navController = rememberSwipeDismissableNavController()
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = NavRoute.HOMESCREEN
    ) {
        composable(NavRoute.HOMESCREEN) {
            HomeScreen(navController, locationUtil)
        }
        composable(NavRoute.SEARCHSCREEN) {
            SearchScreen(navController, locationUtil)
        }
        composable(NavRoute.KEYBOARDSCREEN){
            KeyBoard(navController)
        }
        composable(NavRoute.MAINACTIVITY){
            MainActivity()
        }
        composable(NavRoute.AUTOMATICO){
            WearApp(locationUtil)
        }
    }
}


class UIComponents {

}