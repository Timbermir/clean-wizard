package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.foundation.PokemonReferenceDTOSchema
import kotlinx.serialization.SerialName

@DTO
data class PokemonMoveSchemaDTO(
    val move: PokemonReferenceDTOSchema,
    @SerialName("version_group_details")
    val versionGroupDetails: PokemonMoveVersionGroupDetailsDTOSchema
)