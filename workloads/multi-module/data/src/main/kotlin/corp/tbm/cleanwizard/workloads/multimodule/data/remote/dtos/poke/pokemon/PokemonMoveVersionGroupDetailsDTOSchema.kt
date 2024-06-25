package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.multimodule.data.foundation.PokemonReferenceDTOSchema
import kotlinx.serialization.SerialName

@DTO
data class PokemonMoveVersionGroupDetailsDTOSchema(
    @SerialName("level_learned_at")
    val levelLearnedAt : Int,
    @SerialName("move_learn_method")
    val moveLearnMethod : PokemonReferenceDTOSchema,
    @SerialName("version_group")
    val versionGroup : PokemonReferenceDTOSchema
)