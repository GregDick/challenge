package com.example.greg.challenge

object Model {
    data class Repo(val title: String,
                    val owner: String,
                    val description: String,
                    val size: Int,
                    val numberForks: Int,
                    val numberIssues: Int,
                    val url: String)
}