import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.application
import java.io.File
import java.util.*

object Maintheme {
    val primaryColor= Color(0xff000000)
    val primaryLightColor= Color(0xff2c2c2c)
    val primaryDarkColor= Color(0xff000000)
    val secondaryColor= Color(0xffaeea00)
    val secondaryLightColor= Color(0xffe4ff54)
    val secondaryDarkColor= Color(0xff79b700)
    val primaryTextColor= Color(0xffffffff)
    val secondaryTextColor= Color(0xff000000)
    val lightColors= lightColors(primary = secondaryColor,secondary = primaryColor
        ,background = primaryColor,surface = primaryTextColor
        ,onSurface = secondaryColor,onBackground = primaryTextColor
        ,primaryVariant = primaryLightColor,secondaryVariant = secondaryLightColor
        ,onPrimary = secondaryTextColor,onSecondary = primaryTextColor)
}

fun main()= application{
    val color= Color(0xfffff)
    val file=File(javaClass.classLoader.getResource("colors.xml").file).let {
        //it.writeText("ccx")
        it
       /* val text=it.readText()
        Regex("<color name=\"(.*?)\">#(.*?)</color>").findAll(text).forEach {
            println("val ${it.groups[1]?.value}= Color(0x00${it.groups.get(2)?.value})")
        }*/
    }
    val properties=Properties()
    properties.put("MyKey",false)
    properties.store(file.writer(),"")
}