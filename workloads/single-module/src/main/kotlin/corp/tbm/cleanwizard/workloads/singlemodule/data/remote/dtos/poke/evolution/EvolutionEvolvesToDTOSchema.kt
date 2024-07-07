package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.evolution

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.PokemonReferenceDTOSchema
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.SerialName

@DTO
data class EvolutionEvolvesToDTOSchema(
    @SerialName("evolution_details")
    val evolutionDetails: ImmutableList<EvolutionDetailDTOSchema>,
    @SerialName("is_baby")
    val isBaby: Boolean,
    val species: PokemonReferenceDTOSchema,
)