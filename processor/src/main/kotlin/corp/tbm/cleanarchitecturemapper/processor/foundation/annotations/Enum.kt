package corp.tbm.cleanarchitecturemapper.processor.foundation.annotations

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class StringEnum(val enumEntries: Array<String>, val valueName: String, val values: Array<String>)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class ByteEnum(val enumEntries: Array<String>, val valueName: String, val values: ByteArray)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class ShortEnum(val enumEntries: Array<String>, val valueName: String, val values: ShortArray)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class IntEnum(val enumEntries: Array<String>, val valueName: String, val values: IntArray)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class LongEnum(val enumEntries: Array<String>, val valueName: String, val values: LongArray)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class FloatEnum(val enumEntries: Array<String>, val valueName: String, val values: FloatArray)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class DoubleEnum(val enumEntries: Array<String>, val valueName: String, val values: DoubleArray)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class BooleanEnum(val enumEntries: Array<String>, val valueName: String, val values: BooleanArray)