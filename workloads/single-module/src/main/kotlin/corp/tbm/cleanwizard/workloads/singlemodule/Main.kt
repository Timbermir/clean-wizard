package corp.tbm.cleanwizard.workloads.singlemodule

import androidx.room.Entity
import androidx.room.PrimaryKey
import corp.tbm.cleanwizard.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.singlemodule.address.dtos.AddressDto
import corp.tbm.cleanwizard.workloads.singlemodule.complex.dtos.converters.ComplexDtoConverter
import corp.tbm.cleanwizard.workloads.singlemodule.profile.dtos.ProfileDto
import corp.tbm.cleanwizard.workloads.singlemodule.profile.dtos.converters.ProfileDtoConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

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

class ComplexDTOSchemaConverterTest {
    @Test
    fun testProfileSerialization() {
        val address = AddressDto("123 Main St", "Anytown", "Anystate", "12345", "USA")
        val profile = ProfileDto("John", "Doe", "1990-01-01", listOf(address))
        val json = ComplexDtoConverter.fromProfile(profile)
        val expectedJson = Json.encodeToString(profile)
        assertEquals(expectedJson, json)
    }

    @Test
    fun testProfileDeserialization() {
        val json = """{"firstName":"John","lastName":"Doe","birthDate":"1990-01-01","addresses":[{"street":"123 Main St","city":"Anytown","state":"Anystate","postalCode":"12345","country":"USA"}]}"""
        val profile = ComplexDtoConverter.toProfile(json)
        val expectedProfile = Json.decodeFromString<ProfileDto>(json)
        assertEquals(expectedProfile, profile)
    }

    @Test
    fun testAddressSerialization() {
        val address = listOf(AddressDto("123 Main St", "Anytown", "Anystate", "12345", "USA"))
        val json = ProfileDtoConverter.fromAddresses(address)
        val expectedJson = Json.encodeToString(address)
        assertEquals(expectedJson, json)
    }

    @Test
    fun testAddressDeserialization() {
        val json = """[{"street":"123 Main St","city":"Anytown","state":"Anystate","postalCode":"12345","country":"USA"}]"""
        val addresses = ProfileDtoConverter.toAddresses(json)
        val expectedAddresses = Json.decodeFromString<List<AddressDto>>(json)
        assertEquals(expectedAddresses, addresses)
    }
}