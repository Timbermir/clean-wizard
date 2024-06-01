package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal

import corp.tbm.cleanarchitecturemapper.foundation.annotations.*

enum class EnumType(val annotation: String) {
    STRING(StringEnum::class.qualifiedName.toString()),
    BYTE(ByteEnum::class.qualifiedName.toString()),
    SHORT(ShortEnum::class.qualifiedName.toString()),
    INT(IntEnum::class.qualifiedName.toString()),
    LONG(LongEnum::class.qualifiedName.toString()),
    FLOAT(FloatEnum::class.qualifiedName.toString()),
    DOUBLE(DoubleEnum::class.qualifiedName.toString()),
    BOOLEAN(BooleanEnum::class.qualifiedName.toString());
}