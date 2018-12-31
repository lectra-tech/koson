package io.github.ncomet.koson

@Suppress("ClassName")
object arr : ArrayType() {
    operator fun get(vararg elements: Any?): ArrayType =
        ArrayType(elements.map { toAllowedType(it) }.toList())

    private fun toAllowedType(value: Any?): KosonType =
        when (value) {
            is String -> StringType(value)
            is Number -> NumberType(value)
            is Boolean -> BooleanType(value)
            is ObjectType -> value
            is ArrayType -> value
            is RawJsonType -> value
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

    infix fun String.to(value: String?) {
        objectType.values[this] = if (value == null) NullType else StringType(value)
    }

    infix fun String.to(value: Number?) {
        objectType.values[this] = if (value == null) NullType else NumberType(value)
    }

    infix fun String.to(value: Boolean?) {
        objectType.values[this] = if (value == null) NullType else BooleanType(value)
    }

    infix fun String.to(value: ObjectType) {
        objectType.values[this] = value
    }

    infix fun String.to(value: ArrayType) {
        objectType.values[this] = value
    }

    infix fun String.to(value: RawJsonType) {
        objectType.values[this] = value
    }

    @Suppress("UNUSED_PARAMETER")
    infix fun String.to(value: Nothing?) {
        objectType.values[this] = NullType
    }

    infix fun String.to(value: Any?) {
        objectType.values[this] = if (value == null) NullType else CustomType(value)
    }

    @Deprecated(
            "<this> cannot be used as value inside an obj { }",
            level = DeprecationLevel.ERROR,
            replaceWith = ReplaceWith(expression = "")
    )
    @Suppress("UNUSED_PARAMETER")
    infix fun String.to(value: Koson): Nothing =
            throw IllegalStateException("<this> cannot be used as value inside an obj { }")

    @Deprecated(
            "Key to the left of <to> must be of type String",
            level = DeprecationLevel.ERROR,
            replaceWith = ReplaceWith(expression = "")
    )
    infix fun Any.to(value: Any?): Nothing =
            throw IllegalStateException("key <$this> of ($this to $value) must be of type String")

}

/**
 * Use to render unescaped Json, note that the content will NOT be pretty printed
 * @param validJson the content that will be printed 'as is'. You need to ensure the content is a valid Json String
 */
fun rawJson(validJson: String?): RawJsonType = RawJsonType(validJson)

sealed class KosonType {
    internal abstract fun prettyPrint(level: Int, spaces: Int): String
}

private data class StringType(val value: String) : KosonType() {
    override fun toString(): String = value.quotedEscaped()
    override fun prettyPrint(level: Int, spaces: Int): String = toString()
}

private data class NumberType(val value: Number) : KosonType() {
    override fun toString(): String = value.toString()
    override fun prettyPrint(level: Int, spaces: Int): String = toString()
}

private data class BooleanType(val value: Boolean) : KosonType() {
    override fun toString(): String = value.toString()
    override fun prettyPrint(level: Int, spaces: Int): String = toString()
}

data class ObjectType(internal val values: MutableMap<String, KosonType> = mutableMapOf()) : KosonType() {

    override fun toString(): String =
            values.entries.joinToString(",", "{", "}") { (k, v) -> "\"${k.escapeIllegalChars()}\":$v" }

    fun pretty(spaces: Int = 2): String {
        require(spaces >= 0) { "spaces Int must be positive, but was $spaces." }
        return prettyPrint(0, spaces)
    }

    override fun prettyPrint(level: Int, spaces: Int): String {
        val space = SPACE.repeat((level + 1) * spaces)
        val closingSpace = SPACE.repeat(level * spaces)
        return values.entries.joinToString(",$cr$space", "{$cr$space", "$cr$closingSpace}") { (k, v) ->
            "\"${k.escapeIllegalChars()}\": ${v.prettyPrint(
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
        val space = SPACE.repeat((level + 1) * spaces)
        val closingSpace = SPACE.repeat(level * spaces)
        return values.joinTo(StringBuilder(), ",$cr$space", "[$cr$space", "$cr$closingSpace]") {
            it.prettyPrint(
                    level + 1,
                    spaces
            )
        }.toString()
    }

}

data class RawJsonType(val value: String?) : KosonType() {
    override fun toString(): String = value ?: NULL_PRINT
    override fun prettyPrint(level: Int, spaces: Int): String = toString()

}

private object NullType : KosonType() {
    override fun toString(): String = NULL_PRINT
    override fun prettyPrint(level: Int, spaces: Int): String = toString()
}

private data class CustomType(val value: Any) : KosonType() {
    override fun toString(): String = value.toString().quotedEscaped()
    override fun prettyPrint(level: Int, spaces: Int): String = toString()

}

private val cr = System.lineSeparator()
private const val SPACE = " "
private const val NULL_PRINT = "null"

private const val BACKSLASH = '\\'
private const val DOUBLEQUOTES = '\"'
private val regex = Regex("""[\\"]""")

private fun String.escapeIllegalChars(): String {
    return if (this.contains(BACKSLASH) || this.contains(DOUBLEQUOTES)) {
        regex.replace(this) { mr -> "\\${mr.value}" }
    } else {
        this
    }
}

private fun String.quotedEscaped() = "\"${this.escapeIllegalChars()}\""