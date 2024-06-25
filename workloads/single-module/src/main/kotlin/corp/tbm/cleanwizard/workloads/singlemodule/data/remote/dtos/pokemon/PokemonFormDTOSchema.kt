package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO

@DTO
data class PokemonFormDTOSchema(
    val name: String,
    val url: String
)
