package GsonDate.Weather

class GsonPoi {
    var status: Int? = null
    var message: String? = null
    var count: Int? = null
    var data: List<DataBean>? = null
    var request_id: String? = null

    class DataBean {
        var id: String? = null
        var title: String? = null
        var address: String? = null
        var category: String? = null
        var type: Int? = null
        var location: LocationBean? = null
        var adcode: Int? = null
        var province: String? = null
        var city: String? = null
        var district: String? = null

        class LocationBean {
            var lat: Double? = null
            var lng: Double? = null
        }
    }
}
