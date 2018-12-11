package io.github.ncomet.koson

sealed class KosonType

private data class StringType(val value: String) : KosonType() {
    override fun toString(): String = "\"$value\""
}

private data class NumberType(val value: Number) : KosonType() {
    override fun toString(): String = value.toString()
}

private data class BooleanType(val value: Boolean) : KosonType() {
    override fun toString(): String = value.toString()
}

private data class CustomType(val value: Any) : KosonType() {
    override fun toString(): String = "\"${value.toString().replace("\"", "\\\"")}\""
}

private object NullType : KosonType() {
    override fun toString(): String = "null"
}

data class ObjectType(internal val values: MutableMap<String, KosonType> = mutableMapOf()) : KosonType() {
    override fun toString(): String =
        values.entries.joinToString(",", "{", "}") { (k, v) -> "\"$k\":$v" }
}

open class ArrayType(private val values: List<KosonType> = listOf()) : KosonType() {
    override fun toString(): String = "[${values.joinToString(",")}]"
}

open class EmptyArrayType : ArrayType()

object array : EmptyArrayType() {
    operator fun get(vararg elements: Any?): ArrayType =
        ArrayType(elements.map { toAllowedType(it) }.toList())

    private fun toAllowedType(value: Any?): KosonType {
        return when (value) {
            is String -> StringType(value)
            is Number -> NumberType(value)
            is Boolean -> BooleanType(value)
            is ObjectType -> value
            is ArrayType -> value
            null -> NullType
            else -> CustomType(value)
        }
    }
}

fun obj(block: Koson.() -> Unit): ObjectType {
    val koson = Koson()
    koson.block()
    return koson.objectType
}

class Koson(internal val objectType: ObjectType = ObjectType()) {

    infix fun String.to(value: String) = addValueIfKeyIsAvailable(StringType(value))

    infix fun String.to(value: Number) = addValueIfKeyIsAvailable(NumberType(value))

    infix fun String.to(value: Boolean) = addValueIfKeyIsAvailable(BooleanType(value))

    @Suppress("UNUSED_PARAMETER")
    infix fun String.to(value: Nothing?) = addValueIfKeyIsAvailable(NullType)

    infix fun String.to(value: Any) = addValueIfKeyIsAvailable(CustomType(value))

    infix fun String.to(value: ObjectType) = addValueIfKeyIsAvailable(value)

    infix fun String.to(value: ArrayType) = addValueIfKeyIsAvailable(value)

    @Suppress("UNUSED_PARAMETER")
    infix fun String.to(value: Koson): Nothing =
        throw KosonException("<this> keyword cannot be used as value inside an obj { }")

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
