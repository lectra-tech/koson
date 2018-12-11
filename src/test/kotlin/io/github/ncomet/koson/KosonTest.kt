package io.github.ncomet.koson

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows

@TestInstance(PER_CLASS)
class KosonTest : WithAssertions {

    @Test
    fun `empty object`() {
        assertThat(obj { }.toString()).isEqualTo("{}")
    }

    @Test
    fun `empty array`() {
        assertThat(array.toString()).isEqualTo("[]")
    }

    @Test
    fun `array containing this as a value should render`() {
        array[this]
    }

    object ContainsDoubleQuotes {
        override fun toString(): String = "\"unfor\"tunate\""
    }

    @Test
    fun `object with all possible types of value`() {
        assertThat(obj {
            "string" to "value"
            "int" to 9
            "double" to 7.6
            "float" to 3.2f
            "boolean" to false
            "object" to obj { }
            "emptyArray" to array
            "array" to array["test"]
            "null" to null
            "custom" to ContainsDoubleQuotes
        }.toString()).isEqualTo("{\"string\":\"value\",\"int\":9,\"double\":7.6,\"float\":3.2,\"boolean\":false,\"object\":{},\"emptyArray\":[],\"array\":[\"test\"],\"null\":null,\"custom\":\"\\\"unfor\\\"tunate\\\"\"}")
    }

    @Test
    internal fun `array with all possible types of value`() {
        assertThat(array["value", 9, 7.6, 3.2f, false, obj { }, array, array["test"], null, ContainsDoubleQuotes].toString())
            .isEqualTo("[\"value\",9,7.6,3.2,false,{},[],[\"test\"],null,\"\\\"unfor\\\"tunate\\\"\"]")
    }

    @Nested
    inner class ContainingCases : WithAssertions {
        @Test
        fun `object containing array`() {
            assertThat(obj { "array" to array }.toString()).isEqualTo("{\"array\":[]}")
        }

        @Test
        fun `array containing object`() {
            assertThat(array[obj { }].toString()).isEqualTo("[{}]")
        }

        @Test
        fun `object containing object`() {
            assertThat(obj { "object" to obj { } }.toString()).isEqualTo("{\"object\":{}}")
        }

        @Test
        fun `array containing array`() {
            assertThat(array[array].toString()).isEqualTo("[[]]")
        }

        @Test
        @Suppress("UNUSED_EXPRESSION")
        fun `expression object not containing a Koson_to() should do nothing`() {
            assertThat(obj { "content" }.toString()).isEqualTo("{}")
        }
    }

    @Nested
    inner class MoreComplexCases : WithAssertions {
        @Test
        fun `testing all types in an object`() {
            val obj = obj {
                "key" to 3.4
                "anotherKey" to array["test", "test2", 1, 2.433, true]
                "nullsAreAllowedToo" to null
            }

            assertThat("$obj")
                .isEqualTo("{\"key\":3.4,\"anotherKey\":[\"test\",\"test2\",1,2.433,true],\"nullsAreAllowedToo\":null}")
        }

        @Test
        fun `testing all types in an object inlined`() {
            val obj = obj {
                "key" to 3.4; "anotherKey" to array["test", "test2", 1, 2.433, true]; "nullsAreAllowedToo" to null
            }

            assertThat("$obj")
                .isEqualTo("{\"key\":3.4,\"anotherKey\":[\"test\",\"test2\",1,2.433,true],\"nullsAreAllowedToo\":null}")
        }

        @Test
        fun `testing all types in an array`() {
            val array = array[
                    "koson",
                    obj {
                        "key" to 3.4
                        "anotherKey" to array["test", "test2", 1, 2.333, true, null]
                    }
            ]

            assertThat("$array")
                .isEqualTo("[\"koson\",{\"key\":3.4,\"anotherKey\":[\"test\",\"test2\",1,2.333,true,null]}]")
        }
    }

    @Nested
    inner class ExceptionCases : WithAssertions {

        @Test
        fun `object must throw KosonException when duplicate key`() {
            assertThrows<KosonException> {
                obj {
                    "key" to "myVal"
                    "key" to 1.65
                }
            }
        }

    }

}
