import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.util.*

lateinit var clientWord: OkHttpClient
fun main()= application{
    clientWord= OkHttpClient()
    var list by remember { mutableStateOf(listOf<String>()) }
    var words by remember { mutableStateOf("") }
    MyClipBoard{clipboard, transferable ->
        list=list.toMutableList().let {
            val string=clipboard?.getData(DataFlavor.stringFlavor) as String
            it.add(string)
            words=string
            it.toList()
        }
    }
    Window(onCloseRequest = {exitApplication()},state = WindowState(size = WindowSize(280.dp,800.dp))){
        DesktopMaterialTheme(colors = Maintheme.lightColors){
            Column {
                Box(Modifier.background(MaterialTheme.colors.primaryVariant).weight(1f)){
                    showPostRes(list,Modifier.padding(8.dp)){
                        words= it
                    }
                }
                Box(Modifier.weight(3f)){
                    showTranslate(words,Modifier.padding(4.dp))
                }
            }
        }
    }
}
class MyClipBoard(val update:(Clipboard?, Transferable?)->Unit):ClipboardOwner{
    val clipboard=Toolkit.getDefaultToolkit().systemClipboard
    init {
        clipboard.setContents(clipboard.getContents(null),this)
    }
    override fun lostOwnership(clipboard: Clipboard?, contents: Transferable?) {
        try {
            Thread.sleep(10)
        }catch (e:Exception){
            println(e)
        }
        update(this.clipboard,contents)
        this.clipboard.setContents(this.clipboard.getContents(null),this)
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun showPostRes(list: List<String>, modifier: Modifier =Modifier, clickChange:(String)->Unit){
    Card(onClick = {},modifier=modifier.fillMaxSize(),backgroundColor = MaterialTheme.colors.secondary) {
        Box {
            Column {
                TextButton(onClick = {},modifier.padding(vertical = 4.dp)){
                    Text("剪切板",fontSize = 18.sp)
                }
                if (list.isNotEmpty()) {
                    var select by remember { mutableStateOf(0) }
                    LazyColumn {
                        list.reversed().forEachIndexed { index, str ->
                            item {
                                TextButton(onClick = {
                                    clickChange(str)
                                    select=index
                                }){
                                    Text(str,maxLines = 2,color = if (select==index) MaterialTheme.colors.onSecondary else MaterialTheme.colors.primary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun showTranslate(word: String,modifier: Modifier){
    var text by remember { mutableStateOf("") }
    var worlds by remember { mutableStateOf(listOf<GsonSuggestWorlds.MessageBean>()) }
    var currentTime by remember { mutableStateOf(Date().time) }
    var selectIndex by remember { mutableStateOf(0) }
    fun getWorlds(string: String): List<GsonSuggestWorlds.MessageBean>? {
        val request=Request.Builder()
            .url("https://dict-mobile.iciba.com/interface/index.php?c=word&m=getsuggest&nums=8&is_need_mean=1&word=$string")
            .build()
        val res=clientWord.newCall(request).execute().body?.string()
        val suggestWorlds=gson.fromJson(res,GsonSuggestWorlds::class.java)
        return suggestWorlds.message
    }
    if (!word.isNullOrEmpty()){
        text=word
        getWorlds(word)?.let {
            worlds=it
            selectIndex=0
        }
    }
    Card(onClick = {},backgroundColor = MaterialTheme.colors.secondary,modifier = modifier.fillMaxSize()) {
        Column(Modifier.animateContentSize()) {
            OutlinedTextField(value= text,onValueChange = {
                text=it
                val time=Date().time
                currentTime=time
                GlobalScope.launch {
                    val worldsT=getWorlds(text)
                    if (currentTime==time){
                        if (worldsT != null) {
                            worlds=worldsT
                            selectIndex=0
                        }
                    }
                }
            },label = {
                Text("单词翻译")
            },modifier = Modifier.DropMenuKey(selectIndex,{
                //TODO()
            }){
                selectIndex=it
                worlds.getOrNull(selectIndex)?.key?.let {
                    text=it
                }
            })
            if (worlds.size>0)
                AnimatedVisibility(visible = true){
                    LazyColumn {
                        worlds.forEachIndexed { index, messageBean ->
                            item {
                                TextButton(onClick = {
                                    selectIndex=index
                                }){
                                    Text(messageBean.key+" "+messageBean.paraphrase,maxLines = 1
                                        ,color = if (selectIndex==index) MaterialTheme.colors.onSecondary else MaterialTheme.colors.primary)
                                }
                            }
                        }
                    }
                }
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.DropMenuKey(selectIndex:Int,entry:()->Unit ,updateIndex:(Int)->Unit):Modifier{
    return onKeyEvent {
        when(it.key){
            Key.Tab->{
                updateIndex(selectIndex+1)
                return@onKeyEvent true
            }
            Key.Enter->{
                updateIndex(selectIndex)
                return@onKeyEvent true
            }
            Key.DirectionUp->{
                updateIndex(selectIndex-1)
                entry()
                return@onKeyEvent true
            }
            Key.DirectionDown->{
                updateIndex(selectIndex+1)
                return@onKeyEvent true
            }
        }
        false
    }
}
internal class GsonSuggestWorlds {
    var message: List<MessageBean>? = null
    var status: Int? = null

    class MessageBean {
        var key: String? = null
        var paraphrase: String? = null
        var value: Int? = null
        var means: List<MeansBean>? = null

        class MeansBean {
            var part: String? = null
            var means: List<String>? = null
        }
    }
}

