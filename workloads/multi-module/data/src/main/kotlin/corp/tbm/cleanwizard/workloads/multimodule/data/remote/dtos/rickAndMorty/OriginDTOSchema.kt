package corp.tbm.cleanwizard.workloads.multimodule.data.remote.dtos.rickAndMorty

import androidx.room.Entity
import corp.tbm.cleanwizard.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanwizard.foundation.annotations.DTO

@Entity
@DTO(false, backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class OriginDTOSchema(
    val name: String,
    val url: String
)