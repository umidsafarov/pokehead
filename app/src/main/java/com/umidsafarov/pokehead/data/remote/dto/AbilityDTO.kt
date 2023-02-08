package com.umidsafarov.pokehead.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AbilityDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("effect_entries") val effects: List<DescriptionDTO>
) {
    data class DescriptionDTO(
        @SerializedName("short_effect") val shortDescription: String,
        @SerializedName("effect") val fullDescription: String,
    )
}