package com.example.stylesnapai.navigation

object NavRoutes {
    const val CAMERA = "camera"
    const val RESULTS = "results"
    const val LIBRARY = "library"
    const val SETTINGS = "settings"
    const val DETAIL = "detail/{uri}/{label}"
    fun detailRoute(uri: String, label: String) = "detail/$uri/$label"
}
