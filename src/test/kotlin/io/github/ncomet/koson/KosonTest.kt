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
        assertThat(arrayØ.toString()).isEqualTo("[]")
    }

    @Nested
    inner class ContainingCases : WithAssertions {
        @Test
        fun `object containing array`() {
            assertThat(obj { "array" to arrayØ }.toString()).isEqualTo("{\"array\":[]}")
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
        @Suppress("UNUSED_EXPRESSION")
        fun `object not containing a Koson_to() function`() {
            assertThat(obj { "content" }.toString()).isEqualTo("{}")
        }

        @Test
        fun `array containing array`() {
            assertThat(array[arrayØ].toString()).isEqualTo("[[]]")
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
        fun `array must throw exception when illegal element is inserted`() {
            val message = assertThrows<IllegalArgumentException> { array['c'] }.message
            assertThat(message).isEqualTo("value <c> is not one of allowed JSON value types (String, Number, Boolean, obj{}, array[...], arrayØ or null)")
        }

        @Test
        fun `object must throw exception when illegal element is added`() {
            val message = assertThrows<IllegalArgumentException> { obj { "key" to 'c' } }.message
            assertThat(message).isEqualTo("value <c> is not one of allowed JSON value types (String, Number, Boolean, obj{}, array[...], arrayØ or null)")
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
        @Suppress("UNREACHABLE_CODE")
        fun `object containing a Pair_to() function`() {
            val message = assertThrows<IllegalArgumentException> {
                obj {
                    10 to "element"
                    "correctKey" to 136.36
                }
            }.message
            assertThat(message).isEqualTo("key <10> of (10 to element) is not of type String")
        }

        @Test
        fun `object containing a Pair_to() function with obj {} as key`() {
            val message = assertThrows<IllegalArgumentException> { obj { obj {} to 1.2 } }.message
            assertThat(message).isEqualTo("key <{}> of ({} to 1.2) is not of type String")
        }

        @Test
        fun `object containing a to function with this as a value`() {
            assertThrows<IllegalArgumentException> { obj { "error" to this } }
        }

        @Test
        fun `object containing a Pair_to() function with this as a value`() {
            assertThrows<IllegalArgumentException> { obj { 10 to this } }
        }

        @Test
        fun `object containing a to function with array keyword as value`() {
            val message = assertThrows<IllegalArgumentException> { obj { "error" to array } }.message
            assertThat(message).isEqualTo("<array> keyword cannot be used as value, to describe an empty array, use <arrayØ> instead")
        }

        @Test
        fun `array containing array keyword as value`() {
            val message = assertThrows<IllegalArgumentException> { array[array] }.message
            assertThat(message).isEqualTo("<array> keyword cannot be used as value, to describe an empty array, use <arrayØ> instead")
        }
    }

}
