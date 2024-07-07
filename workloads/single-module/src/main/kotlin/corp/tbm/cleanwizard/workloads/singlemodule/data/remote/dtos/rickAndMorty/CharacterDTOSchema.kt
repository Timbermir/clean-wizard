package corp.tbm.cleanwizard.workloads.singlemodule.data.remote.dtos.rickAndMorty

import corp.tbm.cleanwizard.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.foundation.annotations.StringEnum

@DTO(false, backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class CharacterDTOSchema(
    val id: Int,
    val name: String,
    @StringEnum(enumEntries = ["Alive", "Dead", "Unknown"], enumEntryValues = ["Alive", "Dead", "unknown"])
    val status: String,
    @StringEnum(
        enumEntries = ["Human", "Humanoid", "Alien", "Unknown", "Mythological_Creature"],
        enumEntryValues = ["Human", "Humanoid", "Alien", "Unknown", "Mythological Creature"]
    )
    val species: String,
    @StringEnum(enumEntries = ["Male", "Female", "Unknown"], enumEntryValues = ["Male", "Female", "unknown"])
    val gender: String,
    val origin: OriginDTOSchema,
    val location: OriginDTOSchema,
    val image: String,
    val episodes: List<String>,
    val url: String,
    val created: String
)