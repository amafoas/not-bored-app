package com.example.bored.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
  @GET
  suspend fun getBoredCall(@Url url:String) :Response<BoredResponse>
}