package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.evolution

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.multimodule.data.foundation.PokemonReferenceDTOSchema
import kotlinx.serialization.SerialName

@DTO
data class EvolutionDetailDTOSchema(
    val gender: Any?,
    @SerialName("held_item")
    val heldItem: Any?,
    val item: Any?,
    @SerialName("known_move")
    val knownMove: Any?,
    @SerialName("known_move_type")
    val knownMoveType: Any?,
    val location: Any?,
    @SerialName("min_affection")
    val minAffection: Any?,
    @SerialName("min_beauty")
    val minBeauty: Any?,
    @SerialName("min_happiness")
    val minHappiness: Int,
    @SerialName("min_level")
    val minLevel: Any?,
    @SerialName("needs_overworld_rain")
    val needsOverworldRain: Boolean,
    @SerialName("party_species")
    val partySpecies: Any?,
    @SerialName("party_type")
    val partyType: Any?,
    @SerialName("relative_physical_stats")
    val relativePhysicalStats: Any?,
    @SerialName("time_of_day")
    val timeOfDay: String,
    @SerialName("trade_species")
    val tradeSpecies: Any?,
    val trigger: PokemonReferenceDTOSchema,
    @SerialName("turn_upside_down")
    val turnUpsideDown: Boolean
)
