package com.example.greg.challenge.model

sealed class ResultStatus
data class StatusSuccess(val resultsList: List<Repo>) : ResultStatus()
data class StatusError(val error : String) : ResultStatus()
object StatusLoading : ResultStatus()
