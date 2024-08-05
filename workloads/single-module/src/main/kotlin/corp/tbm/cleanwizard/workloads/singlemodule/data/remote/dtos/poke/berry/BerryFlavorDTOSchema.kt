package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.berry

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.PokemonReferenceDTOSchema

@DTO
data class BerryFlavorDTOSchema(
    val flavor: PokemonReferenceDTOSchema,
    val potency: Int
)