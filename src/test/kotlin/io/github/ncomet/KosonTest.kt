package io.github.ncomet

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class KosonTest : WithAssertions {

    @Nested
    class SimpleCases : WithAssertions {
        @Test
        fun `emptyObject`() {
            assertThat(obj { }.toString()).isEqualTo("{}")
        }

        @Test
        fun `emptyArray`() {
            assertThat(array().toString()).isEqualTo("[]")
        }
    }

    @Nested
    class ContainingCases : WithAssertions {
        @Test
        fun `object containing array`() {
            assertThat(obj { "array" to array() }.toString()).isEqualTo("{\"array\":[]}")
        }

        @Test
        fun `array containing object`() {
            assertThat(array(obj { }).toString()).isEqualTo("[{}]")
        }

        @Test
        fun `object containing object`() {
            assertThat(obj { "object" to obj { } }.toString()).isEqualTo("{\"object\":{}}")
        }

        @Test
        fun `array containing array`() {
            assertThat(array(array()).toString()).isEqualTo("[[]]")
        }
    }

    @Nested
    class MoreComplexCases : WithAssertions {
        @Test
        fun `testing all types in an object`() {
            val obj = obj {
                "key" to 3.4
                "anotherKey" to array("test", "test2", 1, 2.433, true)
                "nullsAreAllowedToo" to null
            }

            assertThat(obj.toString())
                .isEqualTo("{\"key\":3.4,\"anotherKey\":[\"test\",\"test2\",1,2.433,true],\"nullsAreAllowedToo\":null}")
        }

        @Test
        fun `testing all types in an array`() {
            val array = array("aa",
                obj {
                    "key" to 3.4
                    "anotherKey" to array("test", "test2", 1, 2.333, true, null)
                }
            )

            assertThat(array.toString())
                .isEqualTo("[\"aa\",{\"key\":3.4,\"anotherKey\":[\"test\",\"test2\",1,2.333,true,null]}]")
        }
    }
}
