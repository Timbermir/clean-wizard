package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO

@DTO
data class PokemonCriesDTOSchema(
    val latest: String,
    val legacy: String
)