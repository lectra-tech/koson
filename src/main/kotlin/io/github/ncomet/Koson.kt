package io.github.ncomet

sealed class KosonTypes
data class ObjectType(val values: MutableMap<String, KosonTypes> = mutableMapOf()) : KosonTypes() {
    override fun toString(): String = values.entries.joinToString(",", "{", "}") { (k, v) -> "\"$k\":$v" }
}
data class ArrayType(val values: MutableList<KosonTypes> = mutableListOf()) : KosonTypes() {
    override fun toString(): String = "[${values.joinToString(",")}]"
}
private data class StringType(val value: String) : KosonTypes() {
    override fun toString(): String = "\"$value\""
}
private data class NumberType(val value: Double) : KosonTypes() {
    override fun toString(): String = value.toString()
}
private data class IntType(val value: Int) : KosonTypes() {
    override fun toString(): String = value.toString()
}
private data class BooleanType(val value: Boolean) : KosonTypes() {
    override fun toString(): String = value.toString()
}
private object NullType : KosonTypes() {
    override fun toString(): String = "null"
}

fun obj(block: ObjectTypeBuilder.() -> Unit): ObjectType {
    val builder = ObjectTypeBuilder()
    builder.block()
    return builder.objectType
}

fun <T> array(vararg elements: T?): ArrayType {
    val builder = ArrayTypeBuilder()
    elements.forEach { builder.arrayType.values.add(toAllowedType(it)) }
    return builder.arrayType
}

private class ArrayTypeBuilder(val arrayType: ArrayType = ArrayType())

class ObjectTypeBuilder(val objectType: ObjectType = ObjectType()) {
    infix fun <T> String.to(value: T?) {
        if (!objectType.values.containsKey(this)) {
            objectType.values[this] = toAllowedType(value)
        } else {
            throw IllegalArgumentException("Key \"$this\" is already defined for json object")
        }
    }
}

private fun <T> toAllowedType(value: T?) : KosonTypes {
    return when (value) {
        is String -> StringType(value)
        is Int -> IntType(value)
        is Double -> NumberType(value)
        is Boolean -> BooleanType(value)
        is ObjectType -> value
        is ArrayType -> value
        null -> NullType
        else -> throw IllegalArgumentException("Value [$value] has not a recognized JSON compatible type")
    }
}
