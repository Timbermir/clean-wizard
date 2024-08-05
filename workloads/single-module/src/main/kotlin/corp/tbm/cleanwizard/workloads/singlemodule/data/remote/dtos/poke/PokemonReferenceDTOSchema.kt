package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke

import corp.tbm.cleanwizard.foundation.annotations.DTO

@DTO
data class PokemonReferenceDTOSchema(
    val name: String,
    val url: String
)