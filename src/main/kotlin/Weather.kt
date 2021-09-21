@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
import GsonDate.Weather.GsonPoi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import sun.java2d.d3d.D3DGraphicsConfig
import java.awt.GraphicsConfiguration
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

val client=OkHttpClient()
val key="4f9a0172e05341f186475f07da18da65"
val gson=Gson()
@Composable
@OptIn(ExperimentalStdlibApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
fun ApplicationScope.WeatherAndTimeAndLocal(){
    var local by remember { mutableStateOf("") }
    var time by remember { mutableStateOf(0L) }
    var isclick by remember { mutableStateOf(false) }
    var calendar by remember { mutableStateOf("") }
    val timeFormate = SimpleDateFormat("MM-dd hh:mm:ss")
    var localposition by remember { mutableStateOf("") }
    Window(onCloseRequest = { exitApplication() }, state = WindowState()) {
        var screeen by remember { mutableStateOf(window.graphicsConfiguration.defaultTransform.scaleX) }
        window.addPropertyChangeListener("graphicsConfiguration") {
            val newValue=(it.newValue as GraphicsConfiguration).defaultTransform.scaleX
            if (newValue!=screeen)
                screeen=newValue
        }
        Timer().schedule(0, 1000) {
            calendar = Calendar.getInstance().let {
                timeFormate.format(Date())
            }
        }
        DesktopMaterialTheme(colors = Maintheme.lightColors) {
            if (screeen>0f)
                Box(Modifier.fillMaxSize().background(MaterialTheme.colors.primaryVariant)) {
                    Column(modifier = Modifier.fillMaxSize().animateContentSize()) {
                        Row(Modifier.weight(1f).padding(8.dp).fillMaxWidth(0.6f), verticalAlignment = Alignment.CenterVertically) {
                            Card(onClick = {},
                                modifier = Modifier.weight(1f),
                                shape = CutCornerShape(8.dp),
                                backgroundColor = MaterialTheme.colors.secondary
                            ) {
                                Box(Modifier.fillMaxSize().padding(8.dp)) {
                                    Text(
                                        "${calendar}",
                                        modifier = Modifier.align(Alignment.Center),
                                        fontSize = 48.sp,
                                        color = MaterialTheme.colors.onSecondary
                                    )
                                }
                            }
                            //Spacer(Modifier.weight(0.3f))
                        }
                        Box(Modifier.weight(3f)) {
                            if (isclick)
                                WeatherModel(time, local)
                        }
                    }
                    var showEdit by remember { mutableStateOf(true) }
                    Box(Modifier.align(Alignment.TopEnd).fillMaxWidth(0.4f).let { if (!showEdit) it.fillMaxHeight(0.25f) else it }.wrapContentHeight()) {
                        if (showEdit)
                            LocalModel(modifier = Modifier.align(Alignment.Center).fillMaxWidth().padding(8.dp)) {
                                it?.location?.let {
                                    time = Date().time
                                    isclick = true
                                    showEdit=false
                                    local = "${it.lng},${it.lat}"
                                }
                                localposition=""+it?.title
                            }
                        else{
                            Card(onClick = {} ,modifier = Modifier.fillMaxSize().padding(8.dp),shape = RoundedCornerShape(8.dp),backgroundColor = MaterialTheme.colors.secondary) {
                                Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                                    Icon(Icons.Default.LocationOn, "",Modifier.weight(1.25f).align(Alignment.Start).fillMaxSize()
                                    ,tint = MaterialTheme.colors.primary)
                                    Row(Modifier.weight(1f).fillMaxSize(),horizontalArrangement = Arrangement.Center) {
                                        Text(localposition,color = MaterialTheme.colors.onSecondary,modifier = Modifier.align(Alignment.CenterVertically),fontSize = 24.sp)
                                        IconButton(onClick = { showEdit = true },Modifier.fillMaxHeight()) {
                                            Icon(Icons.Default.Edit, "",tint = MaterialTheme.colors.primary,modifier = Modifier.align(
                                                Alignment.CenterVertically).fillMaxSize(0.7f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }
}
@Composable
fun LocalModel(modifier: Modifier=Modifier,click:(GsonPoi.DataBean?)->Unit) {
    Box(modifier.animateContentSize()) {
        Card(modifier = Modifier.wrapContentSize().animateContentSize(),backgroundColor = MaterialTheme.colors.secondary,shape = RoundedCornerShape(8.dp)){
            Box(Modifier.padding(8.dp)){
                getLocations(Modifier.wrapContentSize()) {
                    click(it)
                }
            }
        }
    }
}
@Composable
fun WeatherModel(update:Long,local: String){
    var weatherNow by remember { mutableStateOf(GsonWeatherNow()) }
    var airApi by remember { mutableStateOf(GsonAirApi().now) }
    var threeDay by remember { mutableStateOf(Gson3DayWeather()) }
    var hoursWeather by remember { mutableStateOf(GsonHoursWeather()) }
    var isfinish by remember { mutableStateOf(false) }
    var process by remember { mutableStateOf(0f) }
    val animationProcee by animateFloatAsState(process,ProgressIndicatorDefaults.ProgressAnimationSpec)
    LaunchedEffect(update){
        if (update>0L){
            isfinish=false
            GlobalScope.launch {
                weatherNow = getWeatherNow(local)
                process+=0.25f
                airApi = getAirApi(local)!!
                process+=0.25f
                threeDay=get3Day(local)!!
                process+=0.25f
                hoursWeather=getHoursWeather(local)!!
                process=1f
                isfinish=true
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.primaryVariant).animateContentSize().padding(4.dp)) {
        if (isfinish) {
            showWeather(Modifier.fillMaxSize(), weatherNow, airApi, threeDay, hoursWeather)
        } else {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = animationProcee)
        }
    }
}
private fun getAirApi(local: String): GsonAirApi.NowBean? {
    val request=Request.Builder()
        .url("https://devapi.qweather.com/v7/air/now?location=$local&key=$key").build()
    val response=client.newCall(request).execute()
    val res=gson.fromJson(response.body?.string(),GsonAirApi::class.java)
    return res?.now
}
private fun getWeatherNow(local: String):GsonWeatherNow {
    val request=Request.Builder()
        .url("https://devapi.qweather.com/v7/weather/now?location=${local}&key=$key").get().build()
    val response=client.newCall(request).execute()
    val weatherNow=gson.fromJson(response.body?.string(),GsonWeatherNow::class.java)
    return weatherNow
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun showWeather(
    modifier: Modifier,
    weatherNow: GsonWeatherNow,
    airApi: GsonAirApi.NowBean?,
    threeDay: Gson3DayWeather?,
    hoursWeather: GsonHoursWeather?
){
    Box(modifier){
        Row(Modifier.align(Alignment.Center)) {
            Column(Modifier.fillMaxSize()) {
                /*实时天气概述*/
                Card(modifier = Modifier.weight(1.5f).fillMaxWidth().padding(8.dp),backgroundColor = MaterialTheme.colors.background,shape = RoundedCornerShape(8.dp),onClick = {}) {
                    Row(Modifier.align(Alignment.CenterHorizontally),verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center) {
                        Column(verticalArrangement = Arrangement.Center,modifier= Modifier) {
                            Text("空气质量",fontSize = 18.sp,color = MaterialTheme.colors.primary
                                ,modifier=Modifier.align(Alignment.CenterHorizontally))
                            Text(
                                buildAnnotatedString {
                                                     withStyle(SpanStyle(color = MaterialTheme.colors.onSecondary,fontSize = 48.sp)){
                                                         append(airApi?.aqi?:"E")
                                                     }
                                    append(airApi?.category?:"E")
                                },fontSize = 24.sp
                                ,color = MaterialTheme.colors.primary
                                ,modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                        Column(verticalArrangement = Arrangement.Center,modifier = Modifier) {
                            Icon(painter = painterResource("colorweather/${weatherNow.now?.icon}.png")
                                ,"天气",modifier=Modifier.align(Alignment.CenterHorizontally),tint = MaterialTheme.colors.primary)
                        }
                        Column(verticalArrangement = Arrangement.Center,modifier = Modifier) {
                            Column {
                                Text(weatherNow.now?.text?:"天气获取失败",fontSize = 48.sp,color = MaterialTheme.colors.primary
                                    ,modifier = Modifier.align(Alignment.Start))
                                Row {
                                    Text(weatherNow.now?.temp?:"E",fontSize = 48.sp,color = MaterialTheme.colors.onSecondary)
                                    Column(Modifier.align(Alignment.CenterVertically)) {
                                        Text("℃",color = MaterialTheme.colors.onSecondary)
                                        Text((weatherNow.now?.windDir?:"E")+weatherNow.now?.windScale+"级",
                                            color = MaterialTheme.colors.primary)
                                        //Text("湿度"+weatherNow.now?.humidity+"%",color = MaterialTheme.colors.primary)
                                    }
                                }
                                val precip=hoursWeather?.hourly.let {
                                    it?.first()?.pop?.toInt()?:0+(it?.getOrNull(1)?.pop?.toInt()?:0)
                                }
                                Text("预计未来两小时内${if (precip>0)"\n降雨概率${precip}%" else "无降雨"}",color = MaterialTheme.colors.primary,fontSize = 18.sp)
                            }
                        }
                    }
                }
                /*3天天气预报*/
                Box(Modifier.weight(1f)) {
                    threeDay?.let {
                        showThreeDay(it)
                    }
                }

            }
        }
    }
}
@Composable
private fun getLocations(modifier: Modifier,setArg:(GsonPoi.DataBean?)->Unit){
    fun getcall(location: String,finish:(Response)->Unit){
        val request = Request.Builder()
            .url("https://apis.map.qq.com/ws/place/v1/suggestion/?keyword=$location&key=DBUBZ-5UGO3-SBC3W-3JD55-MHV2J-CXBPU")
            .build()
        val call=client.newCall(request).let {
            it.enqueue(object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call, response: Response) {
                    finish(response)
                }
            })
            it
        }
    }
    Box(modifier) {
        var tip by remember { mutableStateOf("") }
        var location by remember { mutableStateOf(GsonPoi()) }
        var isclick by remember { mutableStateOf(false) }
        Column(modifier = Modifier.animateContentSize()) {
            OutlinedTextField(value = tip, onValueChange = {
                tip = it
                isclick=false
                getcall(tip){
                    location=gson.fromJson(it.body?.string(),GsonPoi::class.java)
                }
            }, label = {
                Text("当前位置")
            },colors = TextFieldDefaults.outlinedTextFieldColors(textColor =MaterialTheme.colors.primary,disabledTextColor = MaterialTheme.colors.primary))
            if (!isclick)
                if (location.count!=null&&location.count!! >0){
                LazyColumn {
                    if (location.data?.size!=null && location.data?.size!! >0)
                        items(location.data?.size!!){i->
                            val poi= location.data?.get(i)
                            TextButton(onClick = {
                                tip=poi?.city+poi?.title
                                setArg(poi)
                                isclick=true
                            }){
                                Text(buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontSize = 18.sp)){
                                        append("${poi?.city}-")
                                    }
                                    append("${poi?.district} ${poi?.title}")
                                })
                            }
                        }
                }
            }
        }
    }
}

private fun get3Day(local: String): Gson3DayWeather? {
    val request=Request.Builder()
        .url("https://devapi.qweather.com/v7/weather/3d?location=$local&key=$key")
        .build()
    val response=client.newCall(request).execute()
    return gson.fromJson(response.body?.string(),Gson3DayWeather::class.java)
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun showThreeDay(weather: Gson3DayWeather) {
    @Composable
    fun RowScope.oneDay(dailyBean: Gson3DayWeather.DailyBean, s: String) {
        Card(
            modifier = Modifier.weight(1f).fillMaxHeight().padding(4.dp),
            onClick = {},
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.primary,
            shape = CutCornerShape(8f)
        ) {
            Column(Modifier.wrapContentSize().padding(8.dp)) {
                Text(s,Modifier.align(Alignment.CenterHorizontally))
                Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Row(Modifier.align(Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically) {
                        Text((dailyBean.tempMax?:"")+"℃", Modifier.weight(2f),textAlign = TextAlign.End)
                        Icon(painterResource("colorweather/${dailyBean.iconDay}.png"), "", modifier = Modifier.weight(1f))
                        Text(dailyBean.textDay?:"", modifier = Modifier.weight(2f))
                    }
                    Row(Modifier.align(Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically) {
                        Text(dailyBean.tempMin+"℃", Modifier.weight(2f), textAlign = TextAlign.End)
                        Icon(painterResource("colorweather/${dailyBean.iconNight}.png"), "", modifier = Modifier.weight(1f))
                        Text("${dailyBean.textNight}", modifier = Modifier.weight(2f))
                    }
                }
            }
        }
    }
    Box{
        Row {
            val calendar=Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH,-1)
            }
            weather.daily?.forEachIndexed { index, dailyBean ->
                calendar.add(Calendar.DAY_OF_MONTH,1)
                oneDay(dailyBean,"${calendar[Calendar.MONTH]+1}月${calendar[Calendar.DAY_OF_MONTH]}日")
            }
        }
    }
}
private fun getHoursWeather(local: String): GsonHoursWeather? {
    val request=Request.Builder()
        .url("https://devapi.qweather.com/v7/weather/24h?location=$local&key=$key")
        .build()
    val response=client.newCall(request).execute()
    val hoursWeather=gson.fromJson(response.body?.string(),GsonHoursWeather::class.java)
    return hoursWeather
}