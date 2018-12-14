package io.github.ncomet.koson

import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import org.assertj.core.api.AbstractAssert

fun assertThat(value: String) = JsonAsserter(value)

class JsonAsserter(actual: String) : AbstractAssert<JsonAsserter, String>(actual, JsonAsserter::class.java) {

    fun isValidJSON(): JsonAsserter {
        try {
            JsonParser().parse(actual)
        } catch (e: JsonSyntaxException) {
            failWithMessage("$actual is not a valid JSON")
        }
        return this
    }

}