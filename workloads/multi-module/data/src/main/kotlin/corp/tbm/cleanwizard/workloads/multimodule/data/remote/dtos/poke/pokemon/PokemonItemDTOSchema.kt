package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.poke.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.poke.PokemonReferenceDTOSchema

@DTO
data class PokemonItemDTOSchema(
    val item : PokemonReferenceDTOSchema,
    val itemVersionDetails : PokemonItemVersionDetailsDTOSchema,

    )
