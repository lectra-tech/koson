package io.github.ncomet

sealed class KosonType

data class ObjectType(val values: MutableMap<String, KosonType> = mutableMapOf()) : KosonType() {
    override fun toString(): String =
            values.entries.joinToString(",", "{", "}") { (k, v) -> "\"$k\":$v" }
}

data class ArrayType(val values: List<KosonType> = listOf()) : KosonType() {
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

val emptyArray: ArrayType = ArrayType(emptyList())

object array {
    operator fun get(vararg elements: Any?) : KosonType =
        ArrayType(elements.map { toAllowedType(it) }.toList())
}

fun obj(block: Koson.() -> Unit): ObjectType {
    val builder = Koson()
    builder.block()
    return builder.objectType
}

class Koson(val objectType: ObjectType = ObjectType()) {

    infix fun <T> String.to(value: T?) {
        if (!objectType.values.containsKey(this)) {
            objectType.values[this] = toAllowedType(value)
        } else {
            throw IllegalArgumentException("Key \"$this\" of ($this to $value) is already defined for json object")
        }
    }

    val array: Koson = this

    operator fun Koson.get(vararg elements: Any?): KosonType =
        ArrayType(elements.map { toAllowedType(it) }.toList())

    infix fun Any.to(value: Any?): Nothing =
        throw IllegalArgumentException("Key \"$this\" of ($this to $value) is not of type String")

}

private fun <T> toAllowedType(value: T?) : KosonType {
    return when (value) {
        is String -> StringType(value)
        is Number -> NumberType(value)
        is Boolean -> BooleanType(value)
        is ObjectType -> value
        is ArrayType -> value
        null -> NullType
        else -> throw IllegalArgumentException("Value [$value] is not one of allowed JSON value types (String, Number, Boolean, obj, array or null)")
    }
}
