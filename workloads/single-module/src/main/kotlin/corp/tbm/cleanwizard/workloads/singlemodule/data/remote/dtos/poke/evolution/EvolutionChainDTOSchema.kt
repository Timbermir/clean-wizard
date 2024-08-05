package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.evolution

import corp.tbm.cleanwizard.foundation.annotations.DTO
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.SerialName

@DTO
data class EvolutionChainDTOSchema(
    @SerialName("evolves_to")
    val evolvesTo: ImmutableList<EvolutionEvolvesToDTOSchema>
)