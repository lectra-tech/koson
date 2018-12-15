package io.github.ncomet.koson

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows

@TestInstance(PER_CLASS)
class KosonTest {

    @Test
    fun `empty object`() {
        val representation = obj { }.toString()
        assertThat(representation).isValidJSON()
        assertThat(representation).isEqualTo("{}")
    }

    @Test
    fun `empty array`() {
        val representation = array.toString()
        assertThat(representation).isValidJSON()
        assertThat(representation).isEqualTo("[]")
    }

    @Test
    fun `array containing this as a value should render`() {
        val representation = array[this]
        assertThat(representation.toString()).isValidJSON()
    }

    object SimpleObject {
        override fun toString(): String = this.javaClass.simpleName
    }

    object ContainsDoubleQuotes {
        override fun toString(): String = "\"unfor\"tunate\""
    }

    @Test
    fun `object with all possible types of value`() {
        val representation = obj {
            "string" to "value"
            "double" to 7.6
            "float" to 3.2f
            "long" to 34L
            "int" to 9
            "char" to 'e'
            "short" to 12.toShort()
            "byte" to 0x32
            "boolean" to false
            "object" to obj { }
            "emptyArray" to array
            "array" to array["test"]
            "null" to null
            "any" to SimpleObject
            "custom" to ContainsDoubleQuotes
        }.toString()
        assertThat(representation).isValidJSON()
        assertThat(representation).isEqualTo("{\"string\":\"value\",\"double\":7.6,\"float\":3.2,\"long\":34,\"int\":9,\"char\":\"e\",\"short\":12,\"byte\":50,\"boolean\":false,\"object\":{},\"emptyArray\":[],\"array\":[\"test\"],\"null\":null,\"any\":\"SimpleObject\",\"custom\":\"\\\"unfor\\\"tunate\\\"\"}")
    }

    @Test
    internal fun `array with all possible types of value`() {
        val representation = array[
                "value",
                7.6,
                3.2f,
                34L,
                9,
                'e',
                12.toShort(),
                0x32,
                false,
                obj { },
                array,
                array["test"],
                null,
                SimpleObject,
                ContainsDoubleQuotes
        ].toString()
        assertThat(representation).isValidJSON()
        assertThat(representation)
            .isEqualTo("[\"value\",7.6,3.2,34,9,\"e\",12,50,false,{},[],[\"test\"],null,\"SimpleObject\",\"\\\"unfor\\\"tunate\\\"\"]")
    }

    @Nested
    inner class ContainingCases {
        @Test
        fun `object containing array`() {
            val representation = obj { "array" to array }.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("{\"array\":[]}")
        }

        @Test
        fun `array containing object`() {
            val representation = array[obj { }].toString()
            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("[{}]")
        }

        @Test
        fun `object containing object`() {
            val representation = obj { "object" to obj { } }.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("{\"object\":{}}")
        }

        @Test
        fun `array containing array`() {
            val representation = array[array].toString()
            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("[[]]")
        }

        @Test
        @Suppress("UNUSED_EXPRESSION")
        fun `object not containing a to() should do nothing`() {
            val representation = obj { "content" }.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("{}")
        }
    }

    @Nested
    inner class MoreComplexCases {
        @Test
        fun `constructing a bit more complex object`() {
            val obj = obj {
                "key" to 3.4
                "anotherKey" to array["test", "test2", 1, 2.433, true]
                "nullsAreAllowedToo" to null
                "array" to array[
                        obj {
                            "double" to 33.4
                            "float" to 345f
                            "long" to 21L
                            "int" to 42
                            "char" to 'a'
                            "byte" to 0xAA
                            "otherArray" to array
                            "simpleObject" to SimpleObject
                        }
                ]
            }
            val representation = obj.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation)
                .isEqualTo("{\"key\":3.4,\"anotherKey\":[\"test\",\"test2\",1,2.433,true],\"nullsAreAllowedToo\":null,\"array\":[{\"double\":33.4,\"float\":345.0,\"long\":21,\"int\":42,\"char\":\"a\",\"byte\":170,\"otherArray\":[],\"simpleObject\":\"SimpleObject\"}]}")
        }

        @Test
        fun `contructing a bit more complex array`() {
            val array = array["koson", 33.4, 345f, 21L, 42, 'a', 0x21,
                    obj {
                        "aKey" to "value"
                        "insideArray" to array
                        "otherArray" to array["element", ContainsDoubleQuotes, obj { }]
                    }
            ]
            val representation = array.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation)
                .isEqualTo("[\"koson\",33.4,345.0,21,42,\"a\",33,{\"aKey\":\"value\",\"insideArray\":[],\"otherArray\":[\"element\",\"\\\"unfor\\\"tunate\\\"\",{}]}]")
        }

        @Test
        fun `testing an object inlined`() {
            val obj =
                obj { "key" to 3.4; "anotherKey" to array["test", "test2", 1, 2.433, true]; "nullsAreAllowedToo" to null }
            val representation = obj.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation)
                .isEqualTo("{\"key\":3.4,\"anotherKey\":[\"test\",\"test2\",1,2.433,true],\"nullsAreAllowedToo\":null}")
        }
    }

    @Nested
    inner class ExceptionCases {

        @Test
        fun `object construction must throw KosonException when duplicate key`() {
            val message = assertThrows<KosonException> {
                obj {
                    "key" to "myVal"
                    "key" to 1.65
                }
            }.message

            assertThat(message!!).isEqualTo("key <key> of (key to 1.65) is already defined for json object")
        }

    }

    @Nested
    inner class PrettyPrints {
        @Test
        fun `must pretty print an object`() {
            val pretty = obj {
                "key" to 3.4
                "anotherKey" to array["test", "test2", 1, 2.433, true]
                "nullsAreAllowedToo" to null
                "array" to array[
                        obj {
                            "double" to 33.4
                            "float" to 345f
                            "long" to 21L
                            "int" to 42
                            "char" to 'a'
                            "byte" to 0xAA
                            "otherArray" to array
                            "simpleObject" to SimpleObject
                            "objectInside" to obj {
                                "to" to 34
                                "too" to "Dog"
                            }
                            "innerArray" to array[
                                    34, 44, "to", null
                            ]
                        }
                ]
            }.pretty()

            assertThat(pretty).isValidJSON()
            assertThat(pretty).isEqualTo(
                "{\n" +
                        "  \"key\": 3.4,\n" +
                        "  \"anotherKey\": [\n" +
                        "    \"test\",\n" +
                        "    \"test2\",\n" +
                        "    1,\n" +
                        "    2.433,\n" +
                        "    true\n" +
                        "  ],\n" +
                        "  \"nullsAreAllowedToo\": null,\n" +
                        "  \"array\": [\n" +
                        "    {\n" +
                        "      \"double\": 33.4,\n" +
                        "      \"float\": 345.0,\n" +
                        "      \"long\": 21,\n" +
                        "      \"int\": 42,\n" +
                        "      \"char\": \"a\",\n" +
                        "      \"byte\": 170,\n" +
                        "      \"otherArray\": [\n" +
                        "        \n" +
                        "      ],\n" +
                        "      \"simpleObject\": \"SimpleObject\",\n" +
                        "      \"objectInside\": {\n" +
                        "        \"to\": 34,\n" +
                        "        \"too\": \"Dog\"\n" +
                        "      },\n" +
                        "      \"innerArray\": [\n" +
                        "        34,\n" +
                        "        44,\n" +
                        "        \"to\",\n" +
                        "        null\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"
            )
        }

        @Test
        fun `must pretty print an array`() {
            val pretty = array[
                    obj {
                        "key" to 3.4
                        "anotherKey" to array["test", "test2", 1, 2.433, true]
                        "nullsAreAllowedToo" to null
                        "array" to array[
                                obj {
                                    "double" to 33.4
                                    "float" to 345f
                                    "long" to 21L
                                    "int" to 42
                                    "char" to 'a'
                                    "byte" to 0xAA
                                    "otherArray" to array
                                    "simpleObject" to SimpleObject
                                    "objectInside" to obj {
                                        "to" to 34
                                        "too" to "Dog"
                                    }
                                    "innerArray" to array[
                                            34, 44, "to", null
                                    ]
                                }
                        ]
                    }
            ].pretty()

            assertThat(pretty).isValidJSON()
            assertThat(pretty).isEqualTo(
                "[\n" +
                        "  {\n" +
                        "    \"key\": 3.4,\n" +
                        "    \"anotherKey\": [\n" +
                        "      \"test\",\n" +
                        "      \"test2\",\n" +
                        "      1,\n" +
                        "      2.433,\n" +
                        "      true\n" +
                        "    ],\n" +
                        "    \"nullsAreAllowedToo\": null,\n" +
                        "    \"array\": [\n" +
                        "      {\n" +
                        "        \"double\": 33.4,\n" +
                        "        \"float\": 345.0,\n" +
                        "        \"long\": 21,\n" +
                        "        \"int\": 42,\n" +
                        "        \"char\": \"a\",\n" +
                        "        \"byte\": 170,\n" +
                        "        \"otherArray\": [\n" +
                        "          \n" +
                        "        ],\n" +
                        "        \"simpleObject\": \"SimpleObject\",\n" +
                        "        \"objectInside\": {\n" +
                        "          \"to\": 34,\n" +
                        "          \"too\": \"Dog\"\n" +
                        "        },\n" +
                        "        \"innerArray\": [\n" +
                        "          34,\n" +
                        "          44,\n" +
                        "          \"to\",\n" +
                        "          null\n" +
                        "        ]\n" +
                        "      }\n" +
                        "    ]\n" +
                        "  }\n" +
                        "]"
            )
        }
    }

}
