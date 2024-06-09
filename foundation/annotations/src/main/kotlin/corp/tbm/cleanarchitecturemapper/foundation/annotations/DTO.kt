package corp.tbm.cleanarchitecturemapper.foundation.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class DTO(val toDomainAsTopLevel: Boolean = true)