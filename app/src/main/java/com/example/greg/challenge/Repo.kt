package com.example.greg.challenge

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class GithubSearchResponse(@field:Json(name = "items") val items : List<Repo?>)

@JsonClass(generateAdapter = true)
data class Repo(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "owner") val owner: Owner?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "size") val size: Int?,
    @field:Json(name = "forks_count") val forks_count: Int?,
    @field:Json(name = "open_issues_count") val open_issues_count: Int?,
    @field:Json(name = "html_url") val html_url: String?
) : Serializable

@JsonClass(generateAdapter = true)
data class Owner(@field:Json(name = "login") val login: String?)