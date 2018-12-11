/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.github.ncomet

import io.github.ncomet.koson.array
import io.github.ncomet.koson.obj
import org.json.JSONArray
import org.json.JSONObject
import org.openjdk.jmh.annotations.Benchmark

open class KosonBenchmark {

    @Benchmark
    fun _0_jsonWithBigArray(): String {
        val json = JSONArray().put(45).put(12.4).put(9.4).put(true).put(false).put(
            true
        ).put(JSONObject.NULL).put(45).put(12.4).put(9.4).put(true).put(
            false
        ).put(true).put(JSONObject.NULL).put(45).put(12.4).put(9.4).put(
            true
        ).put(false).put(true).put(JSONObject.NULL).put(45).put(12.4).put(
            9.4
        ).put(true).put(false).put(true).put(JSONObject.NULL).put(45).put(
            12.4
        ).put(9.4).put(true).put(false).put(true).put(JSONObject.NULL).put("firstElement")
            .put(
                JSONObject()
                    .put("string", "value")
                    .put("int", 9)
                    .put("double", 7.6)
                    .put("float", 3.2f)
                    .put("boolean", false)
                    .put("object", JSONObject())
                    .put("emptyArray", JSONArray())
                    .put("array", JSONArray().put("test"))
                    .put("null", JSONObject.NULL)
                    .put(
                        "otherObj", JSONObject()
                            .put("string", "value")
                            .put("int", 9)
                            .put("double", 7.6)
                            .put("float", 3.2f)
                            .put("boolean", false)
                            .put("object", JSONObject())
                            .put("emptyArray", JSONArray())
                            .put("array", JSONArray().put("test"))
                            .put("null", JSONObject.NULL)
                            .put(
                                "array", JSONArray()
                                    .put(
                                        JSONObject()
                                            .put("string", "value")
                                            .put("int", 9)
                                            .put("double", 7.6)
                                            .put("float", 3.2f)
                                            .put("boolean", false)
                                            .put("object", JSONObject())
                                            .put("emptyArray", JSONArray())
                                            .put("array", JSONArray().put("test"))
                                            .put("null", JSONObject.NULL)
                                    )
                                    .put(
                                        JSONObject()
                                            .put("string", "value")
                                            .put("int", 9)
                                            .put("double", 7.6)
                                            .put("float", 3.2f)
                                            .put("boolean", false)
                                            .put("object", JSONObject())
                                            .put("emptyArray", JSONArray())
                                            .put("array", JSONArray().put("test"))
                                            .put("null", JSONObject.NULL)
                                    )
                                    .put(
                                        JSONObject()
                                            .put("string", "value")
                                            .put("int", 9)
                                            .put("double", 7.6)
                                            .put("float", 3.2f)
                                            .put("boolean", false)
                                            .put("object", JSONObject())
                                            .put("emptyArray", JSONArray())
                                            .put("array", JSONArray().put("test"))
                                            .put("null", JSONObject.NULL)
                                            .put(
                                                "array", JSONArray()
                                                    .put(
                                                        JSONObject()
                                                            .put("string", "value")
                                                            .put("int", 9)
                                                            .put("double", 7.6)
                                                            .put("float", 3.2f)
                                                            .put("boolean", false)
                                                            .put("object", JSONObject())
                                                            .put("emptyArray", JSONArray())
                                                            .put("array", JSONArray().put("test"))
                                                            .put("null", JSONObject.NULL)
                                                    )
                                                    .put(
                                                        JSONObject()
                                                            .put("string", "value")
                                                            .put("int", 9)
                                                            .put("double", 7.6)
                                                            .put("float", 3.2f)
                                                            .put("boolean", false)
                                                            .put("object", JSONObject())
                                                            .put("emptyArray", JSONArray())
                                                            .put(
                                                                "array",
                                                                JSONArray().put(45).put(12.4).put(9.4).put(true).put(
                                                                    false
                                                                ).put(
                                                                    true
                                                                ).put(JSONObject.NULL).put(45).put(12.4).put(9.4).put(
                                                                    true
                                                                ).put(
                                                                    false
                                                                ).put(true).put(JSONObject.NULL).put(45).put(12.4).put(
                                                                    9.4
                                                                ).put(
                                                                    true
                                                                ).put(false).put(true).put(JSONObject.NULL).put(45).put(
                                                                    12.4
                                                                ).put(
                                                                    9.4
                                                                ).put(true).put(false).put(true).put(JSONObject.NULL).put(
                                                                    45
                                                                ).put(
                                                                    12.4
                                                                ).put(9.4).put(true).put(false).put(true).put(JSONObject.NULL)
                                                            )
                                                            .put("null", JSONObject.NULL)
                                                    )
                                            )
                                            .put(
                                                "null", JSONObject()
                                                    .put("string", "value")
                                                    .put("int", 9)
                                                    .put("double", 7.6)
                                                    .put("float", 3.2f)
                                                    .put("boolean", false)
                                                    .put("object", JSONObject())
                                                    .put("emptyArray", JSONArray())
                                                    .put(
                                                        "array", JSONArray()
                                                            .put(
                                                                JSONObject()
                                                                    .put("string", "value")
                                                                    .put("int", 9)
                                                                    .put("double", 7.6)
                                                                    .put("float", 3.2f)
                                                                    .put("boolean", false)
                                                                    .put("object", JSONObject())
                                                                    .put("emptyArray", JSONArray())
                                                                    .put("array", JSONArray().put("test"))
                                                                    .put("null", JSONObject.NULL)
                                                            )
                                                            .put(
                                                                JSONObject()
                                                                    .put("string", "value")
                                                                    .put("int", 9)
                                                                    .put("double", 7.6)
                                                                    .put("float", 3.2f)
                                                                    .put("boolean", false)
                                                                    .put("object", JSONObject())
                                                                    .put("emptyArray", JSONArray())
                                                                    .put(
                                                                        "array",
                                                                        JSONArray().put(45).put(12.4).put(9.4).put(true).put(
                                                                            false
                                                                        ).put(
                                                                            true
                                                                        ).put(JSONObject.NULL).put(45).put(12.4).put(9.4).put(
                                                                            true
                                                                        ).put(
                                                                            false
                                                                        ).put(true).put(JSONObject.NULL).put(45).put(
                                                                            12.4
                                                                        ).put(
                                                                            9.4
                                                                        ).put(
                                                                            true
                                                                        ).put(false).put(true).put(JSONObject.NULL).put(
                                                                            45
                                                                        ).put(
                                                                            12.4
                                                                        ).put(
                                                                            9.4
                                                                        ).put(true).put(false).put(true).put(JSONObject.NULL).put(
                                                                            45
                                                                        ).put(
                                                                            12.4
                                                                        ).put(9.4).put(true).put(false).put(true).put(
                                                                            JSONObject.NULL
                                                                        )
                                                                    )
                                                                    .put("null", JSONObject.NULL)
                                                            )
                                                    )
                                            )
                                    )
                            )
                    ).put(
                        "anotherObject", JSONObject()
                            .put("string", "value")
                            .put("int", 9)
                            .put("double", 7.6)
                            .put("float", 3.2f)
                            .put("boolean", false)
                            .put("object", JSONObject())
                            .put("emptyArray", JSONArray())
                            .put("array", JSONArray().put("test"))
                            .put("null", JSONObject.NULL)
                    )
            )

        return json.toString()
    }

    @Benchmark
    fun _1_kosonWithBigArray(): String {
        val koson =
            array[45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, "firstElement",
                    obj {
                        "string" to "value"
                        "int" to 9
                        "double" to 7.6
                        "float" to 3.2f
                        "boolean" to false
                        "object" to obj { }
                        "emptyArray" to array
                        "array" to array["test"]
                        "null" to null
                        "otherObj" to obj {
                            "string" to "value"
                            "int" to 9
                            "double" to 7.6
                            "float" to 3.2f
                            "boolean" to false
                            "object" to obj { }
                            "emptyArray" to array
                            "array" to array[
                                    obj {
                                        "string" to "value"
                                        "int" to 9
                                        "double" to 7.6
                                        "float" to 3.2f
                                        "boolean" to false
                                        "object" to obj { }
                                        "emptyArray" to array
                                        "array" to array["test"]
                                        "null" to null
                                    },
                                    obj {
                                        "string" to "value"
                                        "int" to 9
                                        "double" to 7.6
                                        "float" to 3.2f
                                        "boolean" to false
                                        "object" to obj { }
                                        "emptyArray" to array
                                        "array" to array["test"]
                                        "null" to null
                                    },
                                    obj {
                                        "string" to "value"
                                        "int" to 9
                                        "double" to 7.6
                                        "float" to 3.2f
                                        "boolean" to false
                                        "object" to obj { }
                                        "emptyArray" to array
                                        "array" to array[
                                                obj {
                                                    "string" to "value"
                                                    "int" to 9
                                                    "double" to 7.6
                                                    "float" to 3.2f
                                                    "boolean" to false
                                                    "object" to obj { }
                                                    "emptyArray" to array
                                                    "array" to array["test"]
                                                    "null" to null
                                                },
                                                obj {
                                                    "string" to "value"
                                                    "int" to 9
                                                    "double" to 7.6
                                                    "float" to 3.2f
                                                    "boolean" to false
                                                    "object" to obj { }
                                                    "emptyArray" to array
                                                    "array" to array[45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null]
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
                                "emptyArray" to array
                                "array" to array[
                                        obj {
                                            "string" to "value"
                                            "int" to 9
                                            "double" to 7.6
                                            "float" to 3.2f
                                            "boolean" to false
                                            "object" to obj { }
                                            "emptyArray" to array
                                            "array" to array["test"]
                                            "null" to null
                                        },
                                        obj {
                                            "string" to "value"
                                            "int" to 9
                                            "double" to 7.6
                                            "float" to 3.2f
                                            "boolean" to false
                                            "object" to obj { }
                                            "emptyArray" to array
                                            "array" to array[45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null]
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
                            "emptyArray" to array
                            "array" to array["test"]
                            "null" to null
                        }
                    }]

        return koson.toString()
    }

    @Benchmark
    fun _2_jsonWithBigObject(): String {
        val json = JSONObject()
            .put("string", "value")
            .put("int", 9)
            .put("double", 7.6)
            .put("float", 3.2f)
            .put("boolean", false)
            .put("object", JSONObject())
            .put("emptyArray", JSONArray())
            .put("array", JSONArray().put("test"))
            .put("null", JSONObject.NULL)
            .put(
                "otherObj", JSONObject()
                    .put("string", "value")
                    .put("int", 9)
                    .put("double", 7.6)
                    .put("float", 3.2f)
                    .put("boolean", false)
                    .put("object", JSONObject())
                    .put("emptyArray", JSONArray())
                    .put("array", JSONArray().put("test"))
                    .put("null", JSONObject.NULL)
                    .put(
                        "array", JSONArray()
                            .put(
                                JSONObject()
                                    .put("string", "value")
                                    .put("int", 9)
                                    .put("double", 7.6)
                                    .put("float", 3.2f)
                                    .put("boolean", false)
                                    .put("object", JSONObject())
                                    .put("emptyArray", JSONArray())
                                    .put("array", JSONArray().put("test"))
                                    .put("null", JSONObject.NULL)
                            )
                            .put(
                                JSONObject()
                                    .put("string", "value")
                                    .put("int", 9)
                                    .put("double", 7.6)
                                    .put("float", 3.2f)
                                    .put("boolean", false)
                                    .put("object", JSONObject())
                                    .put("emptyArray", JSONArray())
                                    .put("array", JSONArray().put("test"))
                                    .put("null", JSONObject.NULL)
                            )
                            .put(
                                JSONObject()
                                    .put("string", "value")
                                    .put("int", 9)
                                    .put("double", 7.6)
                                    .put("float", 3.2f)
                                    .put("boolean", false)
                                    .put("object", JSONObject())
                                    .put("emptyArray", JSONArray())
                                    .put("array", JSONArray().put("test"))
                                    .put("null", JSONObject.NULL)
                                    .put(
                                        "array", JSONArray()
                                            .put(
                                                JSONObject()
                                                    .put("string", "value")
                                                    .put("int", 9)
                                                    .put("double", 7.6)
                                                    .put("float", 3.2f)
                                                    .put("boolean", false)
                                                    .put("object", JSONObject())
                                                    .put("emptyArray", JSONArray())
                                                    .put("array", JSONArray().put("test"))
                                                    .put("null", JSONObject.NULL)
                                            )
                                            .put(
                                                JSONObject()
                                                    .put("string", "value")
                                                    .put("int", 9)
                                                    .put("double", 7.6)
                                                    .put("float", 3.2f)
                                                    .put("boolean", false)
                                                    .put("object", JSONObject())
                                                    .put("emptyArray", JSONArray())
                                                    .put(
                                                        "array",
                                                        JSONArray().put(45).put(12.4).put(9.4).put(true).put(false).put(
                                                            true
                                                        ).put(JSONObject.NULL).put(45).put(12.4).put(9.4).put(true).put(
                                                            false
                                                        ).put(true).put(JSONObject.NULL).put(45).put(12.4).put(9.4).put(
                                                            true
                                                        ).put(false).put(true).put(JSONObject.NULL).put(45).put(12.4).put(
                                                            9.4
                                                        ).put(true).put(false).put(true).put(JSONObject.NULL).put(45).put(
                                                            12.4
                                                        ).put(9.4).put(true).put(false).put(true).put(JSONObject.NULL)
                                                    )
                                                    .put("null", JSONObject.NULL)
                                            )
                                    )
                                    .put(
                                        "null", JSONObject()
                                            .put("string", "value")
                                            .put("int", 9)
                                            .put("double", 7.6)
                                            .put("float", 3.2f)
                                            .put("boolean", false)
                                            .put("object", JSONObject())
                                            .put("emptyArray", JSONArray())
                                            .put(
                                                "array", JSONArray()
                                                    .put(
                                                        JSONObject()
                                                            .put("string", "value")
                                                            .put("int", 9)
                                                            .put("double", 7.6)
                                                            .put("float", 3.2f)
                                                            .put("boolean", false)
                                                            .put("object", JSONObject())
                                                            .put("emptyArray", JSONArray())
                                                            .put("array", JSONArray().put("test"))
                                                            .put("null", JSONObject.NULL)
                                                    )
                                                    .put(
                                                        JSONObject()
                                                            .put("string", "value")
                                                            .put("int", 9)
                                                            .put("double", 7.6)
                                                            .put("float", 3.2f)
                                                            .put("boolean", false)
                                                            .put("object", JSONObject())
                                                            .put("emptyArray", JSONArray())
                                                            .put(
                                                                "array",
                                                                JSONArray().put(45).put(12.4).put(9.4).put(true).put(
                                                                    false
                                                                ).put(
                                                                    true
                                                                ).put(JSONObject.NULL).put(45).put(12.4).put(9.4).put(
                                                                    true
                                                                ).put(
                                                                    false
                                                                ).put(true).put(JSONObject.NULL).put(45).put(12.4).put(
                                                                    9.4
                                                                ).put(
                                                                    true
                                                                ).put(false).put(true).put(JSONObject.NULL).put(45).put(
                                                                    12.4
                                                                ).put(
                                                                    9.4
                                                                ).put(true).put(false).put(true).put(JSONObject.NULL).put(
                                                                    45
                                                                ).put(
                                                                    12.4
                                                                ).put(9.4).put(true).put(false).put(true).put(JSONObject.NULL)
                                                            )
                                                            .put("null", JSONObject.NULL)
                                                    )
                                            )
                                    )
                            )
                    )
            ).put(
                "anotherObject", JSONObject()
                    .put("string", "value")
                    .put("int", 9)
                    .put("double", 7.6)
                    .put("float", 3.2f)
                    .put("boolean", false)
                    .put("object", JSONObject())
                    .put("emptyArray", JSONArray())
                    .put("array", JSONArray().put("test"))
                    .put("null", JSONObject.NULL)
            )

        return json.toString()
    }

    @Benchmark
    fun _3_kosonWithBigObject(): String {
        val koson = obj {
            "string" to "value"
            "int" to 9
            "double" to 7.6
            "float" to 3.2f
            "boolean" to false
            "object" to obj { }
            "emptyArray" to array
            "array" to array["test"]
            "null" to null
            "otherObj" to obj {
                "string" to "value"
                "int" to 9
                "double" to 7.6
                "float" to 3.2f
                "boolean" to false
                "object" to obj { }
                "emptyArray" to array
                "array" to array[
                        obj {
                            "string" to "value"
                            "int" to 9
                            "double" to 7.6
                            "float" to 3.2f
                            "boolean" to false
                            "object" to obj { }
                            "emptyArray" to array
                            "array" to array["test"]
                            "null" to null
                        },
                        obj {
                            "string" to "value"
                            "int" to 9
                            "double" to 7.6
                            "float" to 3.2f
                            "boolean" to false
                            "object" to obj { }
                            "emptyArray" to array
                            "array" to array["test"]
                            "null" to null
                        },
                        obj {
                            "string" to "value"
                            "int" to 9
                            "double" to 7.6
                            "float" to 3.2f
                            "boolean" to false
                            "object" to obj { }
                            "emptyArray" to array
                            "array" to array[
                                    obj {
                                        "string" to "value"
                                        "int" to 9
                                        "double" to 7.6
                                        "float" to 3.2f
                                        "boolean" to false
                                        "object" to obj { }
                                        "emptyArray" to array
                                        "array" to array["test"]
                                        "null" to null
                                    },
                                    obj {
                                        "string" to "value"
                                        "int" to 9
                                        "double" to 7.6
                                        "float" to 3.2f
                                        "boolean" to false
                                        "object" to obj { }
                                        "emptyArray" to array
                                        "array" to array[45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null]
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
                    "emptyArray" to array
                    "array" to array[
                            obj {
                                "string" to "value"
                                "int" to 9
                                "double" to 7.6
                                "float" to 3.2f
                                "boolean" to false
                                "object" to obj { }
                                "emptyArray" to array
                                "array" to array["test"]
                                "null" to null
                            },
                            obj {
                                "string" to "value"
                                "int" to 9
                                "double" to 7.6
                                "float" to 3.2f
                                "boolean" to false
                                "object" to obj { }
                                "emptyArray" to array
                                "array" to array[45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null, 45, 12.4, 9.4, true, false, true, null]
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
                "emptyArray" to array
                "array" to array["test"]
                "null" to null
            }
        }

        return koson.toString()
    }

}
