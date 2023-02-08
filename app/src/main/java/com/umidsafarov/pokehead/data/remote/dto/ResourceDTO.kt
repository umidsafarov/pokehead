package com.umidsafarov.pokehead.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ResourceDTO(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
)