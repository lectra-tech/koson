package com.lectra.koson

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.fail
import java.util.*

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
        val representation = arr.toString()
        assertThat(representation).isValidJSON()
        assertThat(representation).isEqualTo("[]")
    }

    object SimpleObject {
        override fun toString(): String = this.javaClass.simpleName
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
            "emptyArray" to arr
            "array" to arr["test"]
            "arrayFromIterable" to arr[listOf("test")]
            "null" to null
            "custom" to SimpleObject
            "raw" to rawJson("{}")
        }.toString()
        assertThat(representation).isValidJSON()
        assertThat(representation).isEqualTo("""{"string":"value","double":7.6,"float":3.2,"long":34,"int":9,"char":"e","short":12,"byte":50,"boolean":false,"object":{},"emptyArray":[],"array":["test"],"arrayFromIterable":["test"],"null":null,"custom":"SimpleObject","raw":{}}""")
    }

    @Test
    fun `array with all possible types of value`() {
        val representation = arr[
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
                arr,
                arr["test"],
                arr[listOf("test", "from", "iterable")],
                null,
                SimpleObject,
                rawJson("{}")
        ].toString()
        assertThat(representation).isValidJSON()
        assertThat(representation)
            .isEqualTo("""["value",7.6,3.2,34,9,"e",12,50,false,{},[],["test"],["test","from","iterable"],null,"SimpleObject",{}]""")
    }

    object ContainsDoubleQuotesAndBackslashes {
        override fun toString(): String = "\"un\\for\"tuna\\te\""
    }

    object ContainsDoubleQuotes {
        override fun toString(): String = "\"unfor\"tunate\""
    }

    object ContainsBackslashes {
        override fun toString(): String = "\\unfor\\tunate\\"
    }

    @Nested
    inner class EdgeCases {

        @Test
        fun `array containing this as a value should render`() {
            val array = arr[this]

            val representation = array.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).startsWith("[")
            assertThat(representation).endsWith("]")
        }

        @Test
        fun `object with null as key should render`() {
            val obj = obj {
                null to "content"
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("{}")
        }

        @Test
        fun `object with value containing backslash char`() {
            val obj = obj {
                "content" to "va\\lue"
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"content":"va\\lue"}""")
        }

        @Test
        fun `object with custom value containing backslash char`() {
            val obj = obj {
                "content" to ContainsBackslashes
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"content":"\\unfor\\tunate\\"}""")
        }

        @Test
        fun `object with value containing doublequotes char`() {
            val obj = obj {
                "content" to "va\"lue"
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"content":"va\"lue"}""")
        }

        @Test
        fun `object with custom value containing doublequotes char`() {
            val obj = obj {
                "content" to ContainsDoubleQuotes
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"content":"\"unfor\"tunate\""}""")
        }

        @Test
        fun `object with custom value containing backslashes and doublequotes char`() {
            val obj = obj {
                "content" to ContainsDoubleQuotesAndBackslashes
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"content":"\"un\\for\"tuna\\te\""}""")
        }

        @Test
        fun `object with key containing backslash char`() {
            val obj = obj {
                "va\\lue" to "content"
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"va\\lue":"content"}""")
        }

        @Test
        fun `object with key containing doublequotes char`() {
            val obj = obj {
                "va\"lue" to "content"
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"va\"lue":"content"}""")
        }

        @Test
        fun `object with value containing backslashes and doublequotes chars`() {
            val obj = obj {
                "content" to "[}[]}\\,{][,]\"\"\",\",,[,}}}[]],[}#{}"
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"content":"[}[]}\\,{][,]\"\"\",\",,[,}}}[]],[}#{}"}""")
        }

        @Test
        fun `object with key containing backslashes and doublequotes chars`() {
            val obj = obj {
                "[}[]}\\,{][,]\"\"\",\",,[,}}}[]],[}#{}" to "content"
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"[}[]}\\,{][,]\"\"\",\",,[,}}}[]],[}#{}":"content"}""")
        }

        @Test
        fun `array containing backslash char`() {
            val array = arr["va\\lue"]

            val representation = array.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""["va\\lue"]""")
        }

        @Test
        fun `array containing doublequotes char`() {
            val array = arr["va\"lue"]

            val representation = array.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""["va\"lue"]""")
        }

        @Test
        fun `array containing backslashes and doublequotes chars`() {
            val array = arr["[}[]}\\,{][,]\"\"\",\",,[,}}}[]],[}#{}"]

            val representation = array.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""["[}[]}\\,{][,]\"\"\",\",,[,}}}[]],[}#{}"]""")
        }

    }

    @Nested
    inner class ContainingCases {
        @Test
        fun `object containing array`() {
            val representation = obj { "array" to arr }.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"array":[]}""")
        }

        @Test
        fun `array containing object`() {
            val representation = arr[obj { }].toString()
            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("[{}]")
        }

        @Test
        fun `object containing object`() {
            val representation = obj { "object" to obj { } }.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"object":{}}""")
        }

        @Test
        fun `array containing array`() {
            val representation = arr[arr].toString()
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
                "anotherKey" to arr["test", "test2", 1, 2.433, true]
                "nullsAreAllowedToo" to null
                "array" to arr[
                        obj {
                            "double" to 33.4
                            "float" to 345f
                            "long" to 21L
                            "int" to 42
                            "char" to 'a'
                            "byte" to 0xAA
                            "otherArray" to arr
                            "simpleObject" to SimpleObject
                        }
                ]
            }
            val representation = obj.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation)
                .isEqualTo("""{"key":3.4,"anotherKey":["test","test2",1,2.433,true],"nullsAreAllowedToo":null,"array":[{"double":33.4,"float":345.0,"long":21,"int":42,"char":"a","byte":170,"otherArray":[],"simpleObject":"SimpleObject"}]}""")
        }

        @Test
        fun `contructing a bit more complex array`() {
            val array = arr["koson", 33.4, 345f, 21L, 42, 'a', 0x21,
                    obj {
                        "aKey" to "value"
                        "insideArray" to arr
                        "otherArray" to arr["element", SimpleObject, obj { }]
                    }
            ]
            val representation = array.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation)
                .isEqualTo("""["koson",33.4,345.0,21,42,"a",33,{"aKey":"value","insideArray":[],"otherArray":["element","SimpleObject",{}]}]""")
        }

        @Test
        fun `testing an object inlined`() {
            val obj =
                    obj { "key" to 3.4; "anotherKey" to arr["test", "test2", 1, 2.433, true]; "nullsAreAllowedToo" to null }
            val representation = obj.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation)
                .isEqualTo("""{"key":3.4,"anotherKey":["test","test2",1,2.433,true],"nullsAreAllowedToo":null}""")
        }
    }

    @Nested
    inner class ExceptionCases {

        @Test
        fun `obj pretty with negative spaces must throw an IAE`() {
            try {
                obj { }.pretty(-3)
                fail { "No exception was thrown" }
            } catch (iae: IllegalArgumentException) {
                assertThat(iae.message!!).isEqualTo("spaces Int must be positive, but was -3.")
            }
        }

        @Test
        fun `array pretty with negative spaces must throw an IAE`() {
            try {
                arr.pretty(-5)
                fail { "No exception was thrown" }
            } catch (iae: IllegalArgumentException) {
                assertThat(iae.message!!).isEqualTo("spaces Int must be positive, but was -5.")
            }
        }

    }

    @Nested
    inner class RuntimeNullables {

        @Test
        fun `object with all nullable types must render`() {
            val string: String? = null
            val double: Double? = null
            val float: Float? = null
            val long: Long? = null
            val int: Int? = null
            val char: Char? = null
            val short: Short? = null
            val byte: Byte? = null
            val boolean: Boolean? = null
            val date: Date? = null

            val obj = obj {
                "string" to string
                "double" to double
                "float" to float
                "long" to long
                "int" to int
                "char" to char
                "short" to short
                "byte" to byte
                "boolean" to boolean
                "date" to date
            }

            val representation = obj.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"string":null,"double":null,"float":null,"long":null,"int":null,"char":null,"short":null,"byte":null,"boolean":null,"date":null}""")
        }

        @Test
        fun `object with all nullable Java types must render`() {
            val obj = obj {
                "string" to NullableTypes.STRING
                "double" to NullableTypes.DOUBLE
                "float" to NullableTypes.FLOAT
                "long" to NullableTypes.LONG
                "int" to NullableTypes.INT
                "char" to NullableTypes.CHAR
                "short" to NullableTypes.SHORT
                "byte" to NullableTypes.BYTE
                "boolean" to NullableTypes.BOOLEAN
                "date" to NullableTypes.DATE
            }

            val representation = obj.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"string":null,"double":null,"float":null,"long":null,"int":null,"char":null,"short":null,"byte":null,"boolean":null,"date":null}""")
        }

        @Test
        fun `array with all nullable types must render`() {
            val string: String? = null
            val double: Double? = null
            val float: Float? = null
            val long: Long? = null
            val int: Int? = null
            val char: Char? = null
            val short: Short? = null
            val byte: Byte? = null
            val boolean: Boolean? = null
            val date: Date? = null

            val array = arr[
                    string,
                    double,
                    float,
                    long,
                    int,
                    char,
                    short,
                    byte,
                    boolean,
                    date
            ]

            val representation = array.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("[null,null,null,null,null,null,null,null,null,null]")
        }

        @Test
        fun `array with all nullable Java types must render`() {

            val array = arr[
                    NullableTypes.STRING,
                    NullableTypes.DOUBLE,
                    NullableTypes.FLOAT,
                    NullableTypes.LONG,
                    NullableTypes.INT,
                    NullableTypes.CHAR,
                    NullableTypes.SHORT,
                    NullableTypes.BYTE,
                    NullableTypes.BOOLEAN,
                    NullableTypes.DATE
            ]

            val representation = array.toString()
            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("[null,null,null,null,null,null,null,null,null,null]")
        }


    }

    @Nested
    inner class RawJsonContents {

        @Test
        fun `object with inner raw object`() {
            val obj = obj {
                "jsonContent" to rawJson("""{"menu":{"id":"file","value":"File","popup":{"menuitem":[{"value":"New","onclick":"CreateNewDoc()"},{"value":"Open","onclick":"OpenDoc()"},{"value":"Close","onclick":"CloseDoc()"}]}}}""")
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"jsonContent":{"menu":{"id":"file","value":"File","popup":{"menuitem":[{"value":"New","onclick":"CreateNewDoc()"},{"value":"Open","onclick":"OpenDoc()"},{"value":"Close","onclick":"CloseDoc()"}]}}}}""")
        }

        @Test
        fun `array with inner raw object`() {
            val array =
                arr[rawJson("""{"menu":{"id":"file","value":"File","popup":{"menuitem":[{"value":"New","onclick":"CreateNewDoc()"},{"value":"Open","onclick":"OpenDoc()"},{"value":"Close","onclick":"CloseDoc()"}]}}}""")]

            val representation = array.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""[{"menu":{"id":"file","value":"File","popup":{"menuitem":[{"value":"New","onclick":"CreateNewDoc()"},{"value":"Open","onclick":"OpenDoc()"},{"value":"Close","onclick":"CloseDoc()"}]}}}]""")
        }

        @Test
        fun `object with null inner raw object`() {
            val obj = obj {
                "jsonContent" to rawJson(null)
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"jsonContent":null}""")
        }

        @Test
        fun `array with null inner raw object`() {
            val array = arr[rawJson(null)]

            val representation = array.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("[null]")
        }

        @Test
        fun `object with rawjson must be inlined properly when containing windows CR`() {
            val obj = obj {
                "jsonContent" to rawJson("{\r\n  \"menu\":{\r\n    \"id\":\"file\",\r\n    \"value\":\"File\",\r\n    \"popup\":{\r\n      \"menuitem\":[\r\n        {\r\n          \"value\":\"New\",\r\n          \"onclick\":\"CreateNewDoc()\"\r\n        },\r\n        {\r\n          \"value\":\"Open\",\r\n          \"onclick\":\"OpenDoc()\"\r\n        },\r\n        {\r\n          \"value\":\"Close\",\r\n          \"onclick\":\"CloseDoc()\"\r\n        }\r\n      ]\r\n    }\r\n  }\r\n}")
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"jsonContent":{"menu":{"id":"file","value":"File","popup":{"menuitem":[{"value":"New","onclick":"CreateNewDoc()"},{"value":"Open","onclick":"OpenDoc()"},{"value":"Close","onclick":"CloseDoc()"}]}}}}""")
        }

        @Test
        fun `array with rawjson must be inlined properly when containing windows CR`() {
            val array =
                    arr[rawJson("{\r\n  \"menu\":{\r\n    \"id\":\"file\",\r\n    \"value\":\"File\",\r\n    \"popup\":{\r\n      \"menuitem\":[\r\n        {\r\n          \"value\":\"New\",\r\n          \"onclick\":\"CreateNewDoc()\"\r\n        },\r\n        {\r\n          \"value\":\"Open\",\r\n          \"onclick\":\"OpenDoc()\"\r\n        },\r\n        {\r\n          \"value\":\"Close\",\r\n          \"onclick\":\"CloseDoc()\"\r\n        }\r\n      ]\r\n    }\r\n  }\r\n}")]

            val representation = array.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""[{"menu":{"id":"file","value":"File","popup":{"menuitem":[{"value":"New","onclick":"CreateNewDoc()"},{"value":"Open","onclick":"OpenDoc()"},{"value":"Close","onclick":"CloseDoc()"}]}}}]""")
        }

        @Test
        fun `object with rawjson must be inlined properly when containing UNIX CR`() {
            val obj = obj {
                "jsonContent" to rawJson("{\n  \"menu\":{\n    \"id\":\"file\",\n    \"value\":\"File\",\n    \"popup\":{\n      \"menuitem\":[\n        {\n          \"value\":\"New\",\n          \"onclick\":\"CreateNewDoc()\"\n        },\n        {\n          \"value\":\"Open\",\n          \"onclick\":\"OpenDoc()\"\n        },\n        {\n          \"value\":\"Close\",\n          \"onclick\":\"CloseDoc()\"\n        }\n      ]\n    }\n  }\n}")
            }

            val representation = obj.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""{"jsonContent":{"menu":{"id":"file","value":"File","popup":{"menuitem":[{"value":"New","onclick":"CreateNewDoc()"},{"value":"Open","onclick":"OpenDoc()"},{"value":"Close","onclick":"CloseDoc()"}]}}}}""")
        }

        @Test
        fun `array with rawjson must be inlined properly when containing UNIX CR`() {
            val array =
                    arr[rawJson("{\n  \"menu\":{\n    \"id\":\"file\",\n    \"value\":\"File\",\n    \"popup\":{\n      \"menuitem\":[\n        {\n          \"value\":\"New\",\n          \"onclick\":\"CreateNewDoc()\"\n        },\n        {\n          \"value\":\"Open\",\n          \"onclick\":\"OpenDoc()\"\n        },\n        {\n          \"value\":\"Close\",\n          \"onclick\":\"CloseDoc()\"\n        }\n      ]\n    }\n  }\n}")]

            val representation = array.toString()

            assertThat(representation).isValidJSON()
            assertThat(representation).isEqualTo("""[{"menu":{"id":"file","value":"File","popup":{"menuitem":[{"value":"New","onclick":"CreateNewDoc()"},{"value":"Open","onclick":"OpenDoc()"},{"value":"Close","onclick":"CloseDoc()"}]}}}]""")
        }
    }

    @Nested
    inner class PrettyPrints {

        private val cr = System.lineSeparator()!!

        @Test
        fun `must pretty print an object`() {
            val pretty = obj {
                "key" to 3.4
                "anotherKey" to arr["test", "test2", 1, 2.433, true]
                "nullsAreAllowedToo" to null
                "array" to arr[
                        obj {
                            "double" to 33.4
                            "float" to 345f
                            "long" to 21L
                            "int" to 42
                            "char" to 'a'
                            "byte" to 0xAA
                            "otherArray" to arr
                            "simpleObject" to SimpleObject
                            "raw" to rawJson("[]")
                            "objectInside" to obj {
                                "to" to 34
                                "too" to "Dog"
                            }
                            "innerArray" to arr[
                                    34, 44, "to", null
                            ]
                        }
                ]
            }.pretty()

            assertThat(pretty).isValidJSON()
            assertThat(pretty).isEqualTo(
                    "{$cr" +
                            "  \"key\": 3.4,$cr" +
                            "  \"anotherKey\": [$cr" +
                            "    \"test\",$cr" +
                            "    \"test2\",$cr" +
                            "    1,$cr" +
                            "    2.433,$cr" +
                            "    true$cr" +
                            "  ],$cr" +
                            "  \"nullsAreAllowedToo\": null,$cr" +
                            "  \"array\": [$cr" +
                            "    {$cr" +
                            "      \"double\": 33.4,$cr" +
                            "      \"float\": 345.0,$cr" +
                            "      \"long\": 21,$cr" +
                            "      \"int\": 42,$cr" +
                            "      \"char\": \"a\",$cr" +
                            "      \"byte\": 170,$cr" +
                            "      \"otherArray\": [$cr" +
                            "        $cr" +
                            "      ],$cr" +
                            "      \"simpleObject\": \"SimpleObject\",$cr" +
                            "      \"raw\": [],$cr" +
                            "      \"objectInside\": {$cr" +
                            "        \"to\": 34,$cr" +
                            "        \"too\": \"Dog\"$cr" +
                            "      },$cr" +
                            "      \"innerArray\": [$cr" +
                            "        34,$cr" +
                            "        44,$cr" +
                            "        \"to\",$cr" +
                            "        null$cr" +
                            "      ]$cr" +
                            "    }$cr" +
                            "  ]$cr" +
                            "}"
            )
        }

        @Test
        fun `must pretty print an array`() {
            val pretty = arr[
                    obj {
                        "key" to 3.4
                        "anotherKey" to arr["test", "test2", 1, 2.433, true]
                        "nullsAreAllowedToo" to null
                        "array" to arr[
                                obj {
                                    "double" to 33.4
                                    "float" to 345f
                                    "long" to 21L
                                    "int" to 42
                                    "char" to 'a'
                                    "byte" to 0xAA
                                    "otherArray" to arr
                                    "simpleObject" to SimpleObject
                                    "raw" to rawJson("[]")
                                    "objectInside" to obj {
                                        "to" to 34
                                        "too" to "Dog"
                                    }
                                    "innerArray" to arr[
                                            34, 44, "to", null
                                    ]
                                }
                        ]
                    }
            ].pretty()

            assertThat(pretty).isValidJSON()
            assertThat(pretty).isEqualTo(
                    "[$cr" +
                            "  {$cr" +
                            "    \"key\": 3.4,$cr" +
                            "    \"anotherKey\": [$cr" +
                            "      \"test\",$cr" +
                            "      \"test2\",$cr" +
                            "      1,$cr" +
                            "      2.433,$cr" +
                            "      true$cr" +
                            "    ],$cr" +
                            "    \"nullsAreAllowedToo\": null,$cr" +
                            "    \"array\": [$cr" +
                            "      {$cr" +
                            "        \"double\": 33.4,$cr" +
                            "        \"float\": 345.0,$cr" +
                            "        \"long\": 21,$cr" +
                            "        \"int\": 42,$cr" +
                            "        \"char\": \"a\",$cr" +
                            "        \"byte\": 170,$cr" +
                            "        \"otherArray\": [$cr" +
                            "          $cr" +
                            "        ],$cr" +
                            "        \"simpleObject\": \"SimpleObject\",$cr" +
                            "        \"raw\": [],$cr" +
                            "        \"objectInside\": {$cr" +
                            "          \"to\": 34,$cr" +
                            "          \"too\": \"Dog\"$cr" +
                            "        },$cr" +
                            "        \"innerArray\": [$cr" +
                            "          34,$cr" +
                            "          44,$cr" +
                            "          \"to\",$cr" +
                            "          null$cr" +
                            "        ]$cr" +
                            "      }$cr" +
                            "    ]$cr" +
                            "  }$cr" +
                            "]"
            )
        }

        @Test
        internal fun `must pretty print a complex object`() {
            val complexObject = obj {
                "string" to "value"
                "int" to 9
                "double" to 7.6
                "float" to 3.2f
                "boolean" to false
                "object" to obj { }
                "emptyArray" to arr
                "array" to arr["test"]
                "null" to null
                "otherObj" to obj {
                    "string" to "value"
                    "int" to 9
                    "double" to 7.6
                    "float" to 3.2f
                    "boolean" to false
                    "object" to obj { }
                    "emptyArray" to arr
                    "array" to arr[
                            obj {
                                "string" to "value"
                                "int" to 9
                                "double" to 7.6
                                "float" to 3.2f
                                "boolean" to false
                                "object" to obj { }
                                "emptyArray" to arr
                                "array" to arr["test"]
                                "null" to null
                            },
                            obj {
                                "string" to "value"
                                "int" to 9
                                "double" to 7.6
                                "float" to 3.2f
                                "boolean" to false
                                "object" to obj { }
                                "emptyArray" to arr
                                "array" to arr["test"]
                                "null" to null
                            },
                            obj {
                                "string" to "value"
                                "int" to 9
                                "double" to 7.6
                                "float" to 3.2f
                                "boolean" to false
                                "object" to obj { }
                                "emptyArray" to arr
                                "array" to arr[
                                        obj {
                                            "string" to "value"
                                            "int" to 9
                                            "double" to 7.6
                                            "float" to 3.2f
                                            "boolean" to false
                                            "object" to obj { }
                                            "emptyArray" to arr
                                            "array" to arr["test"]
                                            "null" to null
                                        },
                                        obj {
                                            "string" to "value"
                                            "int" to 9
                                            "double" to 7.6
                                            "float" to 3.2f
                                            "boolean" to false
                                            "object" to obj { }
                                            "emptyArray" to arr
                                            "array" to arr[45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null]
                                            "null" to null
                                        }
                                ]
                                "null" to null
                            }
                    ]
                    "null" to obj {
                        "string" to "value"
                        "int" to 9
                        "double" to 7.6
                        "float" to 3.2f
                        "boolean" to false
                        "object" to obj { }
                        "emptyArray" to arr
                        "array" to arr[
                                obj {
                                    "string" to "value"
                                    "int" to 9
                                    "double" to 7.6
                                    "float" to 3.2f
                                    "boolean" to false
                                    "object" to obj { }
                                    "emptyArray" to arr
                                    "array" to arr["test"]
                                    "null" to null
                                },
                                obj {
                                    "string" to "value"
                                    "int" to 9
                                    "double" to 7.6
                                    "float" to 3.2f
                                    "boolean" to false
                                    "object" to obj { }
                                    "emptyArray" to arr
                                    "array" to arr[45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null]
                                    "null" to null
                                }
                        ]
                        "null" to null
                        "onceAgain" to obj {
                            "string" to "value"
                            "int" to 9
                            "double" to 7.6
                            "float" to 3.2f
                            "boolean" to false
                            "object" to obj { }
                            "emptyArray" to arr
                            "array" to arr["test"]
                            "null" to null
                            "otherObj" to obj {
                                "string" to "value"
                                "int" to 9
                                "double" to 7.6
                                "float" to 3.2f
                                "boolean" to false
                                "object" to obj { }
                                "emptyArray" to arr
                                "array" to arr[
                                        obj {
                                            "string" to "value"
                                            "int" to 9
                                            "double" to 7.6
                                            "float" to 3.2f
                                            "boolean" to false
                                            "object" to obj { }
                                            "emptyArray" to arr
                                            "array" to arr["test"]
                                            "null" to null
                                        },
                                        obj {
                                            "string" to "value"
                                            "int" to 9
                                            "double" to 7.6
                                            "float" to 3.2f
                                            "boolean" to false
                                            "object" to obj { }
                                            "emptyArray" to arr
                                            "array" to arr["test"]
                                            "null" to null
                                        },
                                        obj {
                                            "string" to "value"
                                            "int" to 9
                                            "double" to 7.6
                                            "float" to 3.2f
                                            "boolean" to false
                                            "object" to obj { }
                                            "emptyArray" to arr
                                            "array" to arr[
                                                    obj {
                                                        "string" to "value"
                                                        "int" to 9
                                                        "double" to 7.6
                                                        "float" to 3.2f
                                                        "boolean" to false
                                                        "object" to obj { }
                                                        "emptyArray" to arr
                                                        "array" to arr["test"]
                                                        "null" to null
                                                    },
                                                    obj {
                                                        "string" to "value"
                                                        "int" to 9
                                                        "double" to 7.6
                                                        "float" to 3.2f
                                                        "boolean" to false
                                                        "object" to obj { }
                                                        "emptyArray" to arr
                                                        "array" to arr[45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null]
                                                        "null" to null
                                                    }
                                            ]
                                            "null" to null
                                        }
                                ]
                                "null" to obj {
                                    "string" to "value"
                                    "int" to 9
                                    "double" to 7.6
                                    "float" to 3.2f
                                    "boolean" to false
                                    "object" to obj { }
                                    "emptyArray" to arr
                                    "array" to arr[
                                            obj {
                                                "string" to "value"
                                                "int" to 9
                                                "double" to 7.6
                                                "float" to 3.2f
                                                "boolean" to false
                                                "object" to obj { }
                                                "emptyArray" to arr
                                                "array" to arr["test"]
                                                "null" to null
                                            },
                                            obj {
                                                "string" to "value"
                                                "int" to 9
                                                "double" to 7.6
                                                "float" to 3.2f
                                                "boolean" to false
                                                "object" to obj { }
                                                "emptyArray" to arr
                                                "array" to arr[45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null]
                                                "null" to null
                                            }
                                    ]
                                    "null" to null
                                }
                            }
                            "anotherObj" to obj {
                                "string" to "value"
                                "int" to 9
                                "double" to 7.6
                                "float" to 3.2f
                                "boolean" to false
                                "object" to obj { }
                                "emptyArray" to arr
                                "array" to arr["test"]
                                "null" to null
                            }
                        }
                    }
                }
                "anotherObj" to obj {
                    "string" to "value"
                    "int" to 9
                    "double" to 7.6
                    "float" to 3.2f
                    "boolean" to false
                    "object" to obj { }
                    "emptyArray" to arr
                    "array" to arr["test"]
                    "null" to null
                }
            }

            assertThat(complexObject.toString()).isValidJSON()
            assertThat(complexObject.pretty(1)).isValidJSON()
            assertThat(complexObject.pretty(2)).isValidJSON()
            assertThat(complexObject.pretty(3)).isValidJSON()
            assertThat(complexObject.pretty(4)).isValidJSON()
        }
    }

}
