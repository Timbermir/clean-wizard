package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.singlemodule.data.foundation.PokemonReferenceDTOSchema

@DTO
data class PokemonItemDTOSchema(
    val item: PokemonReferenceDTOSchema,
    val itemVersionDetails: PokemonItemVersionDetailsDTOSchema,
)