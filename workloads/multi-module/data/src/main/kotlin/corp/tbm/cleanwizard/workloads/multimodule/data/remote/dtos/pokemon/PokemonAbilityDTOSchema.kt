package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.multimodule.data.foundation.PokemonReferenceDTOSchema
import kotlinx.serialization.SerialName

@DTO
data class PokemonAbilityDTOSchema(
    val pokemonAbilityReference: PokemonReferenceDTOSchema,
    @SerialName("is_hidden")
    val isHidden: Boolean,
    val slot: Int,
)