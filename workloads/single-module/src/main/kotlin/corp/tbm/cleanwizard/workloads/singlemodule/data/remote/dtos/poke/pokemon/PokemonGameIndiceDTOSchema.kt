package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.PokemonReferenceDTOSchema
import kotlinx.serialization.SerialName

@DTO
data class PokemonGameIndiceDTOSchema(
    @SerialName("game_index")
    val gameIndex: Int,
    val version: PokemonReferenceDTOSchema
)
