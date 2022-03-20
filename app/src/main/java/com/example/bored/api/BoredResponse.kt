package com.example.bored.api

data class BoredResponse(var activity: String,
                         var type: String,
                         var participants: Int,
                         var price: Float,
                         var link: String,
                         var key: String,
                         var accessibility: Float)
