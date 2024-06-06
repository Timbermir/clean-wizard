package corp.tbm.cleanarchitecturemapper.foundation.codegen.universal

interface DTOMapper<MODEL> {
    fun toDomain(): MODEL
}

fun <MODEL> List<DTOMapper<MODEL>>.toDomain(): List<MODEL> {
    return map { it.toDomain() }
}