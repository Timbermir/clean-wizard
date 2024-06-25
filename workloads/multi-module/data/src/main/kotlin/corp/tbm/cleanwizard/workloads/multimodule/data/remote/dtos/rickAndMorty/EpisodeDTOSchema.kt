package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.rickAndMorty

import corp.tbm.cleanwizard.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanwizard.foundation.annotations.DTO
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.SerialName

@DTO(false, backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class EpisodeDTOSchema(
    val id: Int,
    val name: String,
    @SerialName("air_date")
    val airDate: String,
    val episode: String,
    val characters: ImmutableList<String>,
    val url: String,
    val created: String
)