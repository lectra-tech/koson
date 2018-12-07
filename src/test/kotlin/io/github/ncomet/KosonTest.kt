package io.github.ncomet

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
        assertThat(emptyArray.toString()).isEqualTo("[]")
    }

    @Nested
    inner class ContainingCases : WithAssertions {
        @Test
        fun `object containing array`() {
            assertThat(obj { "array" to emptyArray }.toString()).isEqualTo("{\"array\":[]}")
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
        fun `object not containing a Koson_to() function`() {
            assertThat(obj { "content" }.toString()).isEqualTo("{}")
        }

        @Test
        fun `array containing array`() {
            assertThat(array[emptyArray].toString()).isEqualTo("[[]]")
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

            assertThat(obj.toString())
                    .isEqualTo("{\"key\":3.4,\"anotherKey\":[\"test\",\"test2\",1,2.433,true],\"nullsAreAllowedToo\":null}")
        }

        @Test
        fun `testing all types in an array`() {
            val array = array["aa",
                    obj {
                        "key" to 3.4
                        "anotherKey" to array["test", "test2", 1, 2.333, true, null]
                    }
            ]

            assertThat(array.toString())
                    .isEqualTo("[\"aa\",{\"key\":3.4,\"anotherKey\":[\"test\",\"test2\",1,2.333,true,null]}]")
        }
    }

    @Nested
    inner class ExceptionCases : WithAssertions {
        @Test
        fun `array must throw exception when illegal element is inserted`() {
            assertThrows<IllegalArgumentException> { array[Pair("D", "D")] }
        }

        @Test
        fun `object must throw exception when illegal element is added`() {
            assertThrows<IllegalArgumentException> { obj { "key" to Pair("D", "D") } }
        }

        @Test
        fun `object must throw exception when illegal when duplicate key`() {
            assertThrows<IllegalArgumentException> {
                obj {
                    "key" to "myVal"
                    "key" to 1.65
                }
            }
        }

        @Test
        fun `object containing a Pair_to() function`() {
            assertThrows<IllegalArgumentException> {
                obj {
                    10 to "element"
                    "correctKey" to 136.36
                }
            }
        }

        @Test
        fun `object containing a to function with this as a value`() {
            assertThrows<IllegalArgumentException> { obj { "daad" to this } }
        }

        @Test
        fun `object containing a Pair_to() function with this as a value`() {
            assertThrows<IllegalArgumentException> { obj { 10 to this } }
        }

    }

}
