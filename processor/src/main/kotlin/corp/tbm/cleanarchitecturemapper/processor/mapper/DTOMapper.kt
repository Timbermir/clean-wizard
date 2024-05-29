package corp.tbm.cleanarchitecturemapper.processor.mapper

interface DTOMapper<MODEL> {
    fun toDomain(): MODEL
}

fun <MODEL> List<DTOMapper<MODEL>>.toDomain() = map { it.toDomain() }