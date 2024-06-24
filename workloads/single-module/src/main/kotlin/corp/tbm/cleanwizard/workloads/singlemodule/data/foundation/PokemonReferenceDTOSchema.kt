package corp.tbm.cleanwizard.workloads.singlemodule.data.foundation

import corp.tbm.cleanwizard.foundation.annotations.DTO

@DTO
data class PokemonReferenceDTOSchema(
    val name: String,
    val url: String
)