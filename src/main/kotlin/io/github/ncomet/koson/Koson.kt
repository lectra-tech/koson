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

private object NullType : KosonType() {
    override fun toString(): String = "null"
}

data class ObjectType(internal val values: MutableMap<String, KosonType> = mutableMapOf()) : KosonType() {
    override fun toString(): String =
        values.entries.joinToString(",", "{", "}") { (k, v) -> "\"$k\":$v" }

    operator fun get(key: String): String = if (values.containsKey(key)) values[key].toString() else ""

}

data class ArrayType(private val values: List<KosonType> = listOf()) : KosonType() {
    override fun toString(): String = "[${values.joinToString(",")}]"
    operator fun get(index: Int): String = if (index in 0 until values.size) values[index].toString() else ""
}

val arrayØ: ArrayType = ArrayType(emptyList())

object array {
    operator fun get(vararg elements: Any?): ArrayType =
        ArrayType(elements.map { toAllowedType(it) }.toList())
}

fun obj(block: Koson.() -> Unit): ObjectType {
    val koson = Koson()
    koson.block()
    return koson.objectType
}

class Koson(internal val objectType: ObjectType = ObjectType()) {

    infix fun String.to(value: String) {
        addValueIfFreeKey(StringType(value))
    }

    infix fun String.to(value: Number) {
        addValueIfFreeKey(NumberType(value))
    }

    infix fun String.to(value: Boolean) {
        addValueIfFreeKey(BooleanType(value))
    }

    @Suppress("UNUSED_PARAMETER")
    infix fun String.to(value: Nothing?) {
        addValueIfFreeKey(NullType)
    }

    infix fun String.to(value: ObjectType) {
        addValueIfFreeKey(value)
    }

    infix fun String.to(value: ArrayType) {
        addValueIfFreeKey(value)
    }

    infix fun String.to(value: Any): Nothing =
        throw IllegalArgumentException("value <$value> of type [${value.javaClass.simpleName}] is not one of allowed JSON value types (String, Number, Boolean, null, obj{}, array[...] or arrayØ)")

    @Suppress("UNUSED_PARAMETER")
    infix fun String.to(value: array): Nothing =
        throw IllegalArgumentException("<array> keyword cannot be used as value, to describe an empty array, use <arrayØ> instead")

    infix fun Any.to(value: Any?): Nothing =
        throw IllegalArgumentException("key <$this> of ($this to $value) must be of type String")

    private fun String.addValueIfFreeKey(type: KosonType) {
        if (!objectType.values.containsKey(this)) {
            objectType.values[this] = type
        } else {
            throw IllegalArgumentException("key <$this> of ($this to $type) is already defined for json object")
        }
    }

}

private fun toAllowedType(value: Any?): KosonType {
    return when (value) {
        is String -> StringType(value)
        is Number -> NumberType(value)
        is Boolean -> BooleanType(value)
        is ObjectType -> value
        is ArrayType -> value
        null -> NullType
        array -> throw IllegalArgumentException("<array> keyword cannot be used as value, to describe an empty array, use <arrayØ> instead")
        else -> throw IllegalArgumentException("value <$value> of type [${value.javaClass.simpleName}] is not one of allowed JSON value types (String, Number, Boolean, null, obj{}, array[...] or arrayØ)")
    }
}
