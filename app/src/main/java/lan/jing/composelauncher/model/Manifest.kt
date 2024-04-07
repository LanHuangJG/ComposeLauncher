package lan.jing.composelauncher.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class Manifest(
    var latest: Latest? = null,
    var versions: List<Version?>? = null
) {
    @Keep
    @Serializable
    data class Latest(
        var release: String? = null, // 1.20.4
        var snapshot: String? = null // 24w14potato
    )

    @Keep
    @Serializable
    data class Version(
        var complianceLevel: Int? = null, // 1
        var id: String? = null, // 24w14potato
        var releaseTime: String? = null, // 2024-04-01T11:07:19+00:00
        var sha1: String? = null, // 21df7f4ba484a6437ab5e9dca0b4dfb5dcefc802
        var time: String? = null, // 2024-04-01T11:14:41+00:00
        var type: String? = null, // snapshot
        var url: String? = null // https://piston-meta.mojang.com/v1/packages/21df7f4ba484a6437ab5e9dca0b4dfb5dcefc802/24w14potato.json
    )
}