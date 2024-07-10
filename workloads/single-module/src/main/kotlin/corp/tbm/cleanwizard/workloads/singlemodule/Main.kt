package corp.tbm.cleanwizard.workloads.singlemodule

import androidx.room.Entity
import androidx.room.PrimaryKey
import corp.tbm.cleanwizard.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanwizard.foundation.annotations.DTO
import kotlinx.serialization.Serializable

fun main() {

}

@Entity
@Serializable
@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class ComplexDTOSchema(
    @PrimaryKey(true)
    val userId: String,
    val userName: String,
    val email: String,
    val profile: ProfileDTOSchema,
    val loh228: AddressDTOSchema
)

@Entity
@Serializable
@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class ProfileDTOSchema(
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val addresses: List<AddressDTOSchema>,
)

@Entity
@Serializable
@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class AddressDTOSchema(
    val street: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String
)