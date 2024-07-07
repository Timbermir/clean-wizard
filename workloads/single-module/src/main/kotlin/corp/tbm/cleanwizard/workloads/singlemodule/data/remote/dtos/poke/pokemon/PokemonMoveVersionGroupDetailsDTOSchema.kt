package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.PokemonReferenceDTOSchema
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