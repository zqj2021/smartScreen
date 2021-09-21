package GsonDate.Weather

class GsonLocal {
    var code: String? = null
    var location: List<LocationBean>? = null
    var refer: ReferBean? = null

    class ReferBean {
        var sources: List<String>? = null
        var license: List<String>? = null
    }

    class LocationBean {
        var name: String? = null
        var id: String? = null
        var lat: String? = null
        var lon: String? = null
        var adm2: String? = null
        var adm1: String? = null
        var country: String? = null
        var tz: String? = null
        var utcOffset: String? = null
        var isDst: String? = null
        var type: String? = null
        var rank: String? = null
        var fxLink: String? = null
    }
}