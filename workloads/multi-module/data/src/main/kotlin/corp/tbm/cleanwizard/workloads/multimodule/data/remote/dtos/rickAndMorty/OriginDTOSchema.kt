package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.rickAndMorty

import androidx.room.Entity
import com.squareup.moshi.JsonClass
import corp.tbm.cleanwizard.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanwizard.foundation.annotations.DTO
import kotlinx.serialization.Serializable

@Entity
@DTO(false, backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class OriginDTOSchema(
    val name: String,
    val url: String
)