package corp.tbm.cleanwizard.workloads.singlemodule

import androidx.room.Entity
import androidx.room.PrimaryKey
import corp.tbm.cleanwizard.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanwizard.foundation.annotations.DTO
import corp.tbm.cleanwizard.workloads.singlemodule.data.AddressDTO
import corp.tbm.cleanwizard.workloads.singlemodule.data.ProfileDTO
import corp.tbm.cleanwizard.workloads.singlemodule.data.converters.ComplexDTOConverter
import corp.tbm.cleanwizard.workloads.singlemodule.data.converters.ProfileDTOConverter
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
        val address = AddressDTO("123 Main St", "Anytown", "Anystate", "12345", "USA")
        val profile = ProfileDTO("John", "Doe", "1990-01-01", listOf(address))
        val json = ComplexDTOConverter.fromProfile(profile)
        val expectedJson = Json.encodeToString(profile)
        assertEquals(expectedJson, json)
    }

    @Test
    fun testProfileDeserialization() {
        val json =
            """{"firstName":"John","lastName":"Doe","birthDate":"1990-01-01","addresses":[{"street":"123 Main St","city":"Anytown","state":"Anystate","postalCode":"12345","country":"USA"}]}"""
        val profile = ComplexDTOConverter.toProfile(json)
        val expectedProfile = Json.decodeFromString<ProfileDTO>(json)
        assertEquals(expectedProfile, profile)
    }

    @Test
    fun testAddressSerialization() {
        val address = listOf(AddressDTO("123 Main St", "Anytown", "Anystate", "12345", "USA"))
        val json = ProfileDTOConverter.fromAddresses(address)
        val expectedJson = Json.encodeToString(address)
        assertEquals(expectedJson, json)
    }

    @Test
    fun testAddressDeserialization() {
        val json =
            """[{"street":"123 Main St","city":"Anytown","state":"Anystate","postalCode":"12345","country":"USA"}]"""
        val addresses = ProfileDTOConverter.toAddresses(json)
        val expectedAddresses = Json.decodeFromString<List<AddressDTO>>(json)
        assertEquals(expectedAddresses, addresses)
    }
}