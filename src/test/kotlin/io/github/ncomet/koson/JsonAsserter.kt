package io.github.ncomet.koson

import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import org.assertj.core.api.StringAssert

fun assertThat(value: String) = JsonAsserter(value)

class JsonAsserter(actual: String) : StringAssert(actual) {

    fun isValidJSON(): JsonAsserter {
        try {
            JsonParser().parse(actual)
        } catch (e: JsonSyntaxException) {
            failWithMessage("$actual is not a valid JSON")
        }
        return this
    }

}