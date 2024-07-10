package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.rickAndMorty

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import corp.tbm.cleanwizard.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.foundation.annotations.StringEnum
import kotlinx.serialization.Serializable

@Entity
@DTO(false, backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class CharacterDTOSchema(
    @PrimaryKey(autoGenerate = true)
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