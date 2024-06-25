package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.berry

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.singlemodule.data.foundation.PokemonReferenceDTOSchema

@DTO
data class BerryFlavorDTOSchema(
    val flavor: PokemonReferenceDTOSchema,
    val potency: Int
)