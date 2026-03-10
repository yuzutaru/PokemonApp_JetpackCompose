package com.yustar.dashboard.data.remote.model

import com.google.gson.annotations.SerializedName

data class PokemonDetailResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("sprites") val sprites: SpritesDto,
    @SerializedName("types") val types: List<TypeSlotDto>,
    @SerializedName("abilities") val abilities: List<AbilitySlotDto> = emptyList()
)

data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String? = null,
    @SerializedName("other") val other: OtherSpritesDto? = null
)

data class OtherSpritesDto(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtworkDto? = null
)

data class OfficialArtworkDto(
    @SerializedName("front_default") val frontDefault: String? = null
)

data class TypeSlotDto(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: TypeDto
)

data class TypeDto(
    @SerializedName("name") val name: String
)

data class AbilitySlotDto(
    @SerializedName("ability") val ability: AbilityDto
)

data class AbilityDto(
    @SerializedName("name") val name: String
)
