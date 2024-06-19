package corp.tbm.cleanwizard

import corp.tbm.cleanwizard.foundation.annotations.BackwardsMappingConfig
import corp.tbm.cleanwizard.foundation.annotations.DTO


fun main() {

}

@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class ComplexDTOSchema(
    val userId: String,
    val userName: String,
    val email: String,
    val profile: ProfileDTOSchema,
    val settings: SettingsDTOSchema,
    val contacts: List<ContactDTOSchema>,
    val transactions: List<TransactionDTOSchema>,
    val notifications: List<NotificationDTOSchema>
)

@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class ProfileDTOSchema(
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val addresses: List<AddressDTOSchema>,
    val phoneNumbers: List<PhoneNumberDTOSchema>
)

@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class AddressDTOSchema(
    val street: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val country: String
)

@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class PhoneNumberDTOSchema(
    val type: String,
    val number: String
)

@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class SettingsDTOSchema(
    val theme: String,
    val notificationsEnabled: Boolean,
    val language: String,
    val privacySettings: PrivacySettingsDTOSchema
)

@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class PrivacySettingsDTOSchema(
    val shareLocation: Boolean,
    val shareProfilePicture: Boolean,
    val shareStatus: Boolean
)

@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class ContactDTOSchema(
    val contactId: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val address: AddressDTOSchema
)

@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class TransactionDTOSchema(
    val transactionId: String,
    val date: String,
    val amount: Double,
    val description: String,
    val items: List<ItemDTOSchema>
)

@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class ItemDTOSchema(
    val itemId: String,
    val itemName: String,
    val quantity: Int,
    val price: Double
)

@DTO(backwardsMappingConfig = BackwardsMappingConfig.FULL_MAPPING)
data class NotificationDTOSchema(
    val notificationId: String,
    val date: String,
    val type: String,
    val message: String,
    val read: Boolean
)