package corp.tbm.cleanwizard.foundation.codegen.universal

import corp.tbm.cleanarchitecturemapper.foundation.annotations.*
import corp.tbm.cleanwizard.foundation.annotations.*
import kotlin.reflect.KClass

enum class EnumType(
    val annotation: KClass<out Annotation>,
    var enumName: String = "",
    var parameterName: String = "",
    val parameterValueSuffix: Char = ' ',
    var enumEntries: ArrayList<String> = arrayListOf(),
    var enumEntryValues: ArrayList<Any> = arrayListOf()
) {
    STRING(StringEnum::class),
    BYTE(ByteEnum::class),
    SHORT(ShortEnum::class),
    INT(IntEnum::class),
    LONG(LongEnum::class, parameterValueSuffix = 'L'),
    FLOAT(FloatEnum::class, parameterValueSuffix = 'f'),
    DOUBLE(DoubleEnum::class),
    BOOLEAN(BooleanEnum::class);
}