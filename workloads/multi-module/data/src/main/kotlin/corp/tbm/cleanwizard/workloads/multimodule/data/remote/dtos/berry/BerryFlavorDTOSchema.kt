package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.berry

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.multimodule.data.foundation.PokemonReferenceDTOSchema

@DTO
data class BerryFlavorDTOSchema(
    val flavor: PokemonReferenceDTOSchema,
    val potency: Int
)