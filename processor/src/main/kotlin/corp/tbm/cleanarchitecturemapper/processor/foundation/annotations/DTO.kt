package corp.tbm.cleanarchitecturemapper.processor.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class DTO(val toDomainAsTopLevel: Boolean = false)