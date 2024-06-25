package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.poke.evolution

import corp.tbm.cleanwizard.foundation.annotations.DTO
import kotlinx.serialization.SerialName

@DTO
data class EvolutionDTOSchema(
    val id: Int,
    @SerialName("baby_trigger_item")
    val babyTriggerItem: Boolean,
    val chain: EvolutionChainDTOSchema
)
