package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.pokemon

import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.multimodule.data.foundation.PokemonReferenceDTOSchema
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.SerialName

@DTO
data class PokemonDTOSchema(
    val id: Int,
    val name: String,
    val order: Int,
    val species: PokemonReferenceDTOSchema,
    val stats: ImmutableList<PokemonStatDTOSchema>,
    val types: ImmutableList<PokemonTypeDTOSchema>,
    val weight: Int,
    @SerialName("is_default")
    val isDefault: Boolean,
    val abilities: ImmutableList<PokemonAbilityDTOSchema>,
    @SerialName("base_experience")
    val baseExperience: Int,
    val cries: PokemonCriesDTOSchema,
    val forms: ImmutableList<PokemonFormDTOSchema>,
    @SerialName("game_indices")
    val gameIndices: ImmutableList<PokemonGameIndiceDTOSchema>,
    val height: Int,
    @SerialName("held_items")
    val heldItems: ImmutableList<PokemonItemDTOSchema>,
    @SerialName("location_area_encounters")
    val locationAreaEncounters: String,
    val moves: ImmutableList<PokemonMoveSchemaDTO>
)