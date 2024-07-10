package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.rickAndMorty

import androidx.room.Entity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import corp.tbm.cleanwizard.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanwizard.foundation.annotations.DTO
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

@Entity
@DTO(false, backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class EpisodeDTOSchema(
    val id: Int,
    val name: String,
    @Json(name = "air_date")
    val airDate: String,
    val episode: String,
    val characters: ImmutableList<String>,
    val url: String,
    val created: String
)