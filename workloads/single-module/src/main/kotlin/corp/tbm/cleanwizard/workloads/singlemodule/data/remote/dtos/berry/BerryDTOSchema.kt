package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.berry

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.singlemodule.data.foundation.PokemonReferenceDTOSchema
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.SerialName

@DTO
data class BerryDTOSchema(
    val id: Int,
    val name: String,
    @SerialName("growth_time")
    val growthTime: Int,
    @SerialName("max_harvest")
    val maxHarvest: Int,
    @SerialName("natural_gift_power")
    val naturalGiftPower: Int,
    @SerialName("natural_gift_type")
    val naturalGiftType: PokemonReferenceDTOSchema,
    val size: Int,
    val smoothness: Int,
    @SerialName("soil-dryness")
    val soilDryness: Int,
    val firmness: PokemonReferenceDTOSchema,
    val flavors: ImmutableList<BerryFlavorDTOSchema>,
    val item: PokemonReferenceDTOSchema
)