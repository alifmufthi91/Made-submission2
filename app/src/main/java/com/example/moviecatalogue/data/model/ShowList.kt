package com.example.moviecatalogue.data.model

import com.google.gson.annotations.SerializedName

data class ShowList(
    @SerializedName("results")
    var list: ArrayList<Show>,
    @SerializedName("total_results")
    var total: Int,
    @SerializedName("total_pages")
    var pages: Int,
    @SerializedName("page")
    var page: Int
)