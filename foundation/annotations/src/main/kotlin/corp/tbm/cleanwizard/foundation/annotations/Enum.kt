package corp.tbm.cleanarchitecturemapper.foundation.annotations

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class StringEnum(
    val enumName: String = "",
    val parameterName: String = "",
    val enumEntries: Array<String>,
    val enumEntryValues: Array<String>
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class ByteEnum(
    val enumName: String = "",
    val parameterName: String = "",
    val enumEntries: Array<String>,
    val enumEntryValues: ByteArray
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class ShortEnum(
    val enumName: String = "",
    val parameterName: String = "",
    val enumEntries: Array<String>,
    val enumEntryValues: ShortArray
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class IntEnum(
    val enumName: String = "",
    val parameterName: String = "",
    val enumEntries: Array<String>,
    val enumEntryValues: IntArray
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class LongEnum(
    val enumName: String = "",
    val parameterName: String = "",
    val enumEntries: Array<String>,
    val enumEntryValues: LongArray
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class FloatEnum(
    val enumName: String = "",
    val parameterName: String = "",
    val enumEntries: Array<String>,
    val enumEntryValues: FloatArray
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class DoubleEnum(
    val enumName: String = "",
    val parameterName: String = "",
    val enumEntries: Array<String>,
    val enumEntryValues: DoubleArray
)

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class BooleanEnum(
    val enumName: String = "",
    val parameterName: String = "",
    val enumEntries: Array<String>,
    val enumEntryValues: BooleanArray
)