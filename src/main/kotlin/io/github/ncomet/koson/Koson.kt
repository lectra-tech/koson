package io.github.ncomet.koson

private val cr = System.lineSeparator()
private const val sp = " "

private fun Any?.escapedOrNull(): String = if (this != null) {
    "\"${this.toString().replace("\\", "\\\\").replace("\"", "\\\"")}\""
} else {
    "null"
}

sealed class KosonType {
    internal abstract fun prettyPrint(level: Int, spaces: Int): String
}

private data class StringType(val value: String?) : KosonType() {
    override fun toString(): String = value.escapedOrNull()
    override fun prettyPrint(level: Int, spaces: Int): String = toString()
}

private data class CustomType(val value: Any?) : KosonType() {
    override fun toString(): String = value.escapedOrNull()
    override fun prettyPrint(level: Int, spaces: Int): String = toString()
}

private data class NumberType(val value: Number?) : KosonType() {
    override fun toString(): String = value.toString()
    override fun prettyPrint(level: Int, spaces: Int): String = toString()
}

private data class BooleanType(val value: Boolean?) : KosonType() {
    override fun toString(): String = value.toString()
    override fun prettyPrint(level: Int, spaces: Int): String = toString()
}

private object NullType : KosonType() {
    override fun toString(): String = "null"
    override fun prettyPrint(level: Int, spaces: Int): String = toString()
}

data class RawJsonType(val value: String?) : KosonType() {
    override fun toString(): String = value ?: "null"
    override fun prettyPrint(level: Int, spaces: Int): String = toString()
}

/**
 * Use to render unescaped Json, note that the content will NOT be pretty printed
 * @param validJson the content that will be printed 'as is'. You need to ensure the content is a valid Json String
 */
fun rawJson(validJson: String?): RawJsonType = RawJsonType(validJson)

data class ObjectType(internal val values: MutableMap<String, KosonType> = mutableMapOf()) : KosonType() {
    override fun toString(): String =
        values.entries.joinToString(",", "{", "}") { (k, v) -> "\"$k\":$v" }

    fun pretty(spaces: Int = 2): String {
        require(spaces >= 0) { "spaces Int must be positive, but was $spaces." }
        return prettyPrint(0, spaces)
    }

    override fun prettyPrint(level: Int, spaces: Int): String {
        val space = sp.repeat((level + 1) * spaces)
        val closingSpace = sp.repeat(level * spaces)
        return values.entries.joinToString(",$cr$space", "{$cr$space", "$cr$closingSpace}") { (k, v) ->
            "\"$k\": ${v.prettyPrint(
                level + 1,
                spaces
            )}"
        }
    }
}

open class ArrayType(private val values: List<KosonType> = emptyList()) : KosonType() {
    override fun toString(): String = values.joinToString(",", "[", "]")

    fun pretty(spaces: Int = 2): String {
        require(spaces >= 0) { "spaces Int must be positive, but was $spaces." }
        return prettyPrint(0, spaces)
    }

    override fun prettyPrint(level: Int, spaces: Int): String {
        val space = sp.repeat((level + 1) * spaces)
        val closingSpace = sp.repeat(level * spaces)
        return values.joinTo(StringBuilder(), ",$cr$space", "[$cr$space", "$cr$closingSpace]") {
            it.prettyPrint(
                level + 1,
                spaces
            )
        }.toString()
    }
}

object array : ArrayType() {
    operator fun get(vararg elements: Any?): ArrayType =
        ArrayType(elements.map { toAllowedType(it) }.toList())

    private fun toAllowedType(value: Any?): KosonType =
        when (value) {
            is String -> StringType(value)
            is Number -> NumberType(value)
            is Boolean -> BooleanType(value)
            is RawJsonType -> value
            is ObjectType -> value
            is ArrayType -> value
            null -> NullType
            else -> CustomType(value)
        }
}

fun obj(block: Koson.() -> Unit): ObjectType {
    val koson = Koson()
    koson.block()
    return koson.objectType
}

class Koson(internal val objectType: ObjectType = ObjectType()) {

    infix fun String.to(value: String?) = addValueIfKeyIsAvailable(StringType(value))

    infix fun String.to(value: Number?) = addValueIfKeyIsAvailable(NumberType(value))

    infix fun String.to(value: Boolean?) = addValueIfKeyIsAvailable(BooleanType(value))

    @Suppress("UNUSED_PARAMETER")
    infix fun String.to(value: Nothing?) = addValueIfKeyIsAvailable(NullType)

    infix fun String.to(value: Any?) = addValueIfKeyIsAvailable(CustomType(value))

    infix fun String.to(value: RawJsonType) = addValueIfKeyIsAvailable(value)

    infix fun String.to(value: ObjectType) = addValueIfKeyIsAvailable(value)

    infix fun String.to(value: ArrayType) = addValueIfKeyIsAvailable(value)

    @Deprecated(
        "<this> cannot be used as value inside an obj { }",
        level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith(expression = "")
    )
    @Suppress("UNUSED_PARAMETER")
    infix fun String.to(value: Koson): Nothing =
        throw KosonException("<this> cannot be used as value inside an obj { }")

    @Deprecated(
        "Key to the left of <to> must be of type String",
        level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith(expression = "")
    )
    infix fun Any.to(value: Any?): Nothing =
        throw KosonException("key <$this> of ($this to $value) must be of type String")

    private fun String.addValueIfKeyIsAvailable(type: KosonType) {
        if (!objectType.values.containsKey(this)) {
            objectType.values[this] = type
        } else {
            throw KosonException("key <$this> of ($this to $type) is already defined for json object")
        }
    }

}

class KosonException(message: String) : RuntimeException(message)
