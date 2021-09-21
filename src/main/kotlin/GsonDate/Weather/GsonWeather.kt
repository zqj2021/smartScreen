class GsonWeatherNow {
    var code: String? = null
    var updateTime: String? = null
    var fxLink: String? = null
    var now: NowBean? = null
    var refer: ReferBean? = null

    class NowBean {
        var obsTime: String? = null
        var temp: String? = null
        var icon: String? = null
        var text: String? = null
        var windDir: String? = null
        var windScale: String? = null
        var humidity: String? = null
        var precip: String? = null
        var pressure: String? = null
    }

    class ReferBean {
        var sources: List<String>? = null
        var license: List<String>? = null
    }
}
class GsonAirApi {
    var code: String? = null
    var updateTime: String? = null
    var fxLink: String? = null
    var now: NowBean? = null
    var station: List<StationBean>? = null
    var refer: ReferBean? = null

    class NowBean {
        var pubTime: String? = null
        var aqi: String? = null
        var level: String? = null
        var category: String? = null
        var primary: String? = null
        var pm10: String? = null
        var pm2p5: String? = null
        var no2: String? = null
        var so2: String? = null
        var co: String? = null
        var o3: String? = null
    }

    class ReferBean {
        var sources: List<String>? = null
        var license: List<String>? = null
    }

    class StationBean {
        var pubTime: String? = null
        var name: String? = null
        var id: String? = null
        var aqi: String? = null
        var level: String? = null
        var category: String? = null
        var primary: String? = null
        var pm10: String? = null
        var pm2p5: String? = null
        var no2: String? = null
        var so2: String? = null
        var co: String? = null
        var o3: String? = null
    }
}
class Gson3DayWeather {
    var code: String? = null
    var updateTime: String? = null
    var fxLink: String? = null
    var daily: List<DailyBean>? = null
    var refer: ReferBean? = null

    class ReferBean {
        var sources: List<String>? = null
        var license: List<String>? = null
    }

    class DailyBean {
        var fxDate: String? = null
        var tempMax: String? = null
        var tempMin: String? = null
        var iconDay: String? = null
        var iconNight: String? = null
        var textDay: String? = null
        var textNight: String? = null
        var windDirDay: String? = null
        var windScaleDay: String? = null
        var windScaleNight: String? = null
    }
}
class GsonHoursWeather {
    var code: String? = null
    var updateTime: String? = null
    var fxLink: String? = null
    var hourly: List<HourlyBean>? = null
    var refer: ReferBean? = null

    class ReferBean {
        var sources: List<String>? = null
        var license: List<String>? = null
    }

    class HourlyBean {
        var fxTime: String? = null
        var temp: String? = null
        var icon: String? = null
        var text: String? = null
        var wind360: String? = null
        var windDir: String? = null
        var windScale: String? = null
        var windSpeed: String? = null
        var humidity: String? = null
        var pop: String? = null
        var precip: String? = null
        var pressure: String? = null
        var cloud: String? = null
        var dew: String? = null
    }
}



