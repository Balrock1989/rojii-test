package com.tests

import com.BaseTest
import com.api.Status
import com.data.DataBank
import com.models.response.users.SingleUserModel
import kotlinx.serialization.json.Json
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*
import org.hamcrest.text.MatchesPattern.matchesPattern
import org.json.JSONObject
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class SingleUserTest : BaseTest() {

    @DataProvider
    fun positiveUsersId(): Array<Array<Int>> {
        return arrayOf(
                arrayOf(0),
                arrayOf(1),
                arrayOf(12),
                arrayOf(13),
        )
    }

    @Test(description = "Валидация деталей ответа для одного пользовател", dataProvider = "positiveUsersId")
    fun validateUsersDetailsTest(userId: Int) {
        val usersJson: JSONObject = get(DataBank.USERS_URL.get() + "/$userId", Status.OK.code)
        val user = Json.decodeFromString(SingleUserModel.serializer(), usersJson.toString())
        assertThat(user.ad.company, equalTo(DataBank.AD_COMPANY.get()))
        assertThat(user.ad.text, equalTo(DataBank.AD_TEXT.get()))
        assertThat(user.ad.url, equalTo(DataBank.AD_URL.get()))
        assertThat(user.data.id, greaterThanOrEqualTo(1))
        assertThat(user.data.first_name, matchesPattern(DataBank.NAME_PATTERN.get()))
        assertThat(user.data.last_name, matchesPattern(DataBank.NAME_PATTERN.get()))
        assertThat(user.data.email, matchesPattern(DataBank.EMAIL_PATTERN.get()))
        assertThat(user.data.avatar, matchesPattern(DataBank.URL_PATTERN.get()))
    }
}