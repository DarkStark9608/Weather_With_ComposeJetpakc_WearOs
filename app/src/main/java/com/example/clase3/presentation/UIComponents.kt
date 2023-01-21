package com.example.clase3.presentation

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.clase3.R

import com.example.clase3.presentation.theme.Clase3Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.*



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
    modifier: Modifier=Modifier, title:String, weatherDes:String, time:String, temp:Double){
    AppCard(
        onClick={ /*TODO*/} , appName= {Text("Clima", color=Color.White)},
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
            top = 32.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 32.dp),
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
                            temp = locationUtil.data.value.temp)
                    }
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



class UIComponents {

}