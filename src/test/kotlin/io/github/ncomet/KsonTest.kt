package io.github.ncomet

import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Test

class KsonTest {

    @Test
    fun name() {
        val json = obj {
            "key" to 3.4
            "anotherKey" to array("test", "test2", 1, 2.433, true)
        }
        val toto = array("aa",
            obj {
                "key" to 3.4
                "anotherKey" to array("test", "test2", 1, 2.433, true)
            }
        )
        println(json.toString())
        println(toto.toString())

        println(
            JSONObject().put(
                "anotherKey", JSONArray()
                    .put("test")
                    .put("test2")
                    .put(1)
                    .put(2.433)
                    .put(true)
            )
                .put("key", 3.4)
                .put("key", "wrong")
        )

    }
}
