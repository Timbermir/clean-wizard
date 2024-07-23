package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.rickAndMorty

import androidx.room.Entity
import corp.tbm.cleanwizard.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.foundation.annotations.StringEnum
import kotlinx.collections.immutable.ImmutableList

@Entity
@DTO(false, backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class LocationDTOSchema(
    val id: Int,
    val name: String,
    @StringEnum(
        enumEntries = ["Planet", "Space Station", "Cluster", "Microverse", "Resort", "TV", "Fantasy", "Dream"],
        enumEntryValues = ["Planet", "Space Station", "Cluster", "Microverse", "Resort", "TV", "Fantasy", "Dream"]
    )
    val type: String,
    val dimension: String,
    val residents: ImmutableList<String>,
    val url: String,
    val created: String
)
