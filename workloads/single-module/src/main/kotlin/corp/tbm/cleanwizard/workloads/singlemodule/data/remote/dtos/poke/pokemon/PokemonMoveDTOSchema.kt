package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.PokemonReferenceDTOSchema
import kotlinx.serialization.SerialName

@DTO
data class PokemonMoveDTOSchema(
    val move: PokemonReferenceDTOSchema,
    @SerialName("version_group_details")
    val versionGroupDetails: PokemonMoveVersionGroupDetailsDTOSchema
)