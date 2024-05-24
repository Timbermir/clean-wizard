package corp.tbm.cleanarchitecturemapper.processor.mapper

interface DTOMapper<MODEL> {
    fun toDomain(): MODEL
}