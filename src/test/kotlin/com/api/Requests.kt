package com.api

import com.models.general.dataObjects.BaseModel
import io.qameta.allure.Step
import kotlinx.serialization.json.Json
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.http.promisesBody
import org.json.JSONObject
import java.io.IOException

/*** Реализация запросов*/
open class Requests : ApiRequestHelper() {

    @Step("POST запрос")
    fun post(url: String, body: String, expectedCode: Int): JSONObject {
        val requestBody: RequestBody = body.toRequestBody(mediaType)
        val request = Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build()
        client.newCall(request).execute().use {
            checkStatus(it.code, expectedCode)
            val response: String = it.body!!.string()
            return if (response != "") JSONObject(response) else JSONObject()
        }
    }

    @Step("GET запрос")
    fun get(url: String, expectedCode: Int): JSONObject {
        val request = Request.Builder()
                .url(url)
                .headers(headers)
                .get()
                .build()
        client.newCall(request).execute().use {
            checkStatus(it.code, expectedCode)
            val response: String = it.body!!.string()
            return if (response != "") JSONObject(response) else JSONObject()
        }
    }

    @Step("PATCH запрос")
    fun patch(url: String, body: String, expectedCode: Int): JSONObject {
        val requestBody: RequestBody = body.toRequestBody(mediaType)
        val request = Request.Builder()
                .url(url)
                .headers(headers)
                .patch(requestBody)
                .build()
        client.newCall(request).execute().use {
            checkStatus(it.code, expectedCode)
            val response: String = it.body!!.string()
            return if (response != "") JSONObject(response) else JSONObject()
        }
    }

    @Step("DELETE запрос")
    fun delete(url: String, expectedCode: Int): JSONObject {
        val request = Request.Builder()
                .url(url)
                .headers(headers)
                .delete()
                .build()
        client.newCall(request).execute().use {
            checkStatus(it.code, expectedCode)
            val response: String = it.body!!.string()
            return if (response != "") JSONObject(response) else JSONObject()
        }
    }

    @Step("PUT запрос")
    fun put(url: String, body: String, expectedCode: Int): JSONObject {
        val requestBody: RequestBody = body.toRequestBody(mediaType)
        val request = Request.Builder()
                .url(url)
                .headers(headers)
                .put(requestBody)
                .build()
        client.newCall(request).execute().use {
            checkStatus(it.code, expectedCode)
            val response: String = it.body!!.string()
            return if (response != "") JSONObject(response) else JSONObject()
        }
    }
    /*** Создание объекта в системе*/
    fun <T : BaseModel<T>> create(url: String, model: T): T {
        val requestBody: RequestBody = model.getBody().toRequestBody(mediaType)
        val request = Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build()
        client.newCall(request).execute().use {
            if (!it.isSuccessful) throw IOException("Unexpected code $it")
            val response: String = it.body!!.string()
            return if (response != "") Json.decodeFromString(model.getSerializer(), response) else throw IOException()
        }
    }
}