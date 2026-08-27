package br.com.curso.portalrickandmorty.remote.dto

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("results")
    val results: List<LocationDto>
)

data class LocationDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("dimension")
    val dimension: String
)