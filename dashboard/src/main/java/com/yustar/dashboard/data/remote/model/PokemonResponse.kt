package com.yustar.dashboard.data.remote.model

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<PokemonDto>
)

data class PokemonDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
