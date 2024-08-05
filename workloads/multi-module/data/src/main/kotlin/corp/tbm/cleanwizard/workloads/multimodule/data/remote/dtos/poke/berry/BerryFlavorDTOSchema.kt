package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.poke.berry

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.poke.PokemonReferenceDTOSchema

@DTO
data class BerryFlavorDTOSchema(
    val flavor: PokemonReferenceDTOSchema,
    val potency: Int
)