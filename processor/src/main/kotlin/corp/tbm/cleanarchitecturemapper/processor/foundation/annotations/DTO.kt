package corp.tbm.cleanarchitecturemapper.processor.foundation.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class DTO(val toDomainAsTopLevel: Boolean = false)