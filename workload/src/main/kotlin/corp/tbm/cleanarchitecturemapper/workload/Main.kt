package corp.tbm.cleanarchitecturemapper.workload

import corp.tbm.cleanarchitecturemapper.processor.foundation.annotations.BooleanEnum
import corp.tbm.cleanarchitecturemapper.processor.foundation.annotations.DTO
import corp.tbm.cleanarchitecturemapper.processor.foundation.annotations.IntEnum
import corp.tbm.cleanarchitecturemapper.processor.foundation.annotations.StringEnum

fun main() {
}

@DTO
data class DebilDTO(
    @IntEnum(["Daun", "Debil", "Kon4"], "debil", [1, 2, 3])
    val debil: Int = 1,
    @StringEnum(["slkdfjls", "dfkljgdjl"], "kon4", ["sosi", "4len"])
    val debikk : String
)

@DTO
data class Kon4DTO(
    @StringEnum(["slkdfjls", "dfkljgdjl"], "kon4", ["sosi", "4len"])
    val chlen: String
)

@DTO

data class Kon4DsgldkfTO(
    @BooleanEnum(["slkdfjls", "dfkljgdjl"], "kon4", [false, false])
    val chelentatno: Boolean
)