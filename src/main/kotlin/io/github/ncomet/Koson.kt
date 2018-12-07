package io.github.ncomet

sealed class KosonType

data class ObjectType(val values: MutableMap<String, KosonType> = mutableMapOf()) : KosonType() {
    override fun toString(): String =
            values.entries.joinToString(",", "{", "}") { (k, v) -> "\"$k\":$v" }
}
data class ArrayType(val values: MutableList<KosonType> = mutableListOf()) : KosonType() {
    override fun toString(): String = "[${values.joinToString(",")}]"
}
private data class StringType(val value: String) : KosonType() {
    override fun toString(): String = "\"$value\""
}

private data class NumberType(val value: Number) : KosonType() {
    override fun toString(): String = value.toString()
}
private data class BooleanType(val value: Boolean) : KosonType() {
    override fun toString(): String = value.toString()
}
private object NullType : KosonType() {
    override fun toString(): String = "null"
}

val emptyArray: ArrayType = ArrayType()

object array {
    operator fun get(vararg elements: Any?) : KosonType =
            ArrayType(elements.map { toAllowedType(it) }.toMutableList())
}

fun obj(block: ObjectTypeBuilder.() -> Unit): ObjectType {
    val builder = ObjectTypeBuilder()
    builder.block()
    return builder.objectType
}

class ObjectTypeBuilder(val objectType: ObjectType = ObjectType()) {

    infix fun <T> String.to(value: T?) {
        if (!objectType.values.containsKey(this)) {
            objectType.values[this] = toAllowedType(value)
        } else {
            throw IllegalArgumentException("Key \"$this\" is already defined for json object")
        }
    }

    val array : ObjectTypeBuilder = this

    operator fun ObjectTypeBuilder.get(vararg elements: Any?) : KosonType =
            ArrayType(elements.map { toAllowedType(it) }.toMutableList())

    infix fun Any.to(ignored: Any?): Nothing =
            throw IllegalArgumentException("Using Pair.to() function is not allowed by Koson")
}

private fun <T> toAllowedType(value: T?) : KosonType {
    return when (value) {
        is String -> StringType(value)
        is Number -> NumberType(value)
        is Boolean -> BooleanType(value)
        is ObjectType -> value
        is ArrayType -> value
        null -> NullType
        else -> throw IllegalArgumentException("Value [$value] has not a recognized JSON compatible type")
    }
}
