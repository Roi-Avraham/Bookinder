package com.example.bookinder.api

import com.example.bookinder.models.DefaultResponse
import com.example.bookinder.models.LoginResponse
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Field

interface Api {
    @FormUrlEncoded
    @POST("createuser")
    fun createUser(
            @Field("user name") username: String,
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("age")age: String,
            @Field("phone_number")phone_number: String
    ):Call<DefaultResponse>

    @FormUrlEncoded
    @POST("userlogin")
    fun userLogin(
            @Field("email") email: String,
            @Field("password") password: String
    ):Call<LoginResponse>
}