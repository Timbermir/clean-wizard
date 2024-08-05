package corp.tbm.cleanwizard.workloads.multimodule.data.foundation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

@Serializable
data class PagingResponseDTO<T>(
    val count: Int,
    val next: String,
    val previous: String,
    val results: ImmutableList<T>
)