package lan.jing.composelauncher.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import kotlinx.serialization.Contextual
import kotlinx.serialization.json.JsonArray

@Keep
@Serializable
data class Version(
    var arguments: Arguments? = null,
    var assetIndex: AssetIndex? = null,
    var assets: String? = null, // 12
    var complianceLevel: Int? = null, // 1
    var downloads: Downloads? = null,
    var id: String? = null, // 1.20.4
    var javaVersion: JavaVersion? = null,
    var libraries: List<Library?>? = null,
    var logging: Logging? = null,
    var mainClass: String? = null, // net.minecraft.client.main.Main
    var minimumLauncherVersion: Int? = null, // 21
    var releaseTime: String? = null, // 2023-12-07T12:56:20+00:00
    var time: String? = null, // 2023-12-07T12:56:20+00:00
    var type: String? = null // release
) {
    @Keep
    @Serializable
    data class Arguments(
        var game: JsonArray? = null,
        var jvm: JsonArray? = null
    )

    @Keep
    @Serializable
    data class AssetIndex(
        var id: String? = null, // 12
        var sha1: String? = null, // fe9f91e17663b04d6ca468444bb315cacd46688b
        var size: Int? = null, // 436400
        var totalSize: Int? = null, // 627676957
        var url: String? = null // https://piston-meta.mojang.com/v1/packages/fe9f91e17663b04d6ca468444bb315cacd46688b/12.json
    )

    @Keep
    @Serializable
    data class Downloads(
        var client: Client? = null,
        @SerialName("client_mappings")
        var clientMappings: ClientMappings? = null,
        var server: Server? = null,
        @SerialName("server_mappings")
        var serverMappings: ServerMappings? = null
    ) {
        @Keep
        @Serializable
        data class Client(
            var sha1: String? = null, // fd19469fed4a4b4c15b2d5133985f0e3e7816a8a
            var size: Int? = null, // 24445539
            var url: String? = null // https://piston-data.mojang.com/v1/objects/fd19469fed4a4b4c15b2d5133985f0e3e7816a8a/client.jar
        )

        @Keep
        @Serializable
        data class ClientMappings(
            var sha1: String? = null, // be76ecc174ea25580bdc9bf335481a5192d9f3b7
            var size: Int? = null, // 8897012
            var url: String? = null // https://piston-data.mojang.com/v1/objects/be76ecc174ea25580bdc9bf335481a5192d9f3b7/client.txt
        )

        @Keep
        @Serializable
        data class Server(
            var sha1: String? = null, // 8dd1a28015f51b1803213892b50b7b4fc76e594d
            var size: Int? = null, // 49150256
            var url: String? = null // https://piston-data.mojang.com/v1/objects/8dd1a28015f51b1803213892b50b7b4fc76e594d/server.jar
        )

        @Keep
        @Serializable
        data class ServerMappings(
            var sha1: String? = null, // c1cafe916dd8b58ed1fe0564fc8f786885224e62
            var size: Int? = null, // 6797462
            var url: String? = null // https://piston-data.mojang.com/v1/objects/c1cafe916dd8b58ed1fe0564fc8f786885224e62/server.txt
        )
    }

    @Keep
    @Serializable
    data class JavaVersion(
        var component: String? = null, // java-runtime-gamma
        var majorVersion: Int? = null // 17
    )

    @Keep
    @Serializable
    data class Library(
        var downloads: Downloads? = null,
        var name: String? = null, // ca.weblite:java-objc-bridge:1.1
        var rules: List<Rule?>? = null
    ) {
        @Keep
        @Serializable
        data class Downloads(
            var artifact: Artifact? = null
        ) {
            @Keep
            @Serializable
            data class Artifact(
                var path: String? = null, // ca/weblite/java-objc-bridge/1.1/java-objc-bridge-1.1.jar
                var sha1: String? = null, // 1227f9e0666314f9de41477e3ec277e542ed7f7b
                var size: Int? = null, // 1330045
                var url: String? = null // https://libraries.minecraft.net/ca/weblite/java-objc-bridge/1.1/java-objc-bridge-1.1.jar
            )
        }

        @Keep
        @Serializable
        data class Rule(
            var action: String? = null, // allow
            var os: Os? = null
        ) {
            @Keep
            @Serializable
            data class Os(
                var name: String? = null // osx
            )
        }
    }

    @Keep
    @Serializable
    data class Logging(
        var client: Client? = null
    ) {
        @Keep
        @Serializable
        data class Client(
            var argument: String? = null, // -Dlog4j.configurationFile=${path}
            var `file`: File? = null,
            var type: String? = null // log4j2-xml
        ) {
            @Keep
            @Serializable
            data class File(
                var id: String? = null, // client-1.12.xml
                var sha1: String? = null, // bd65e7d2e3c237be76cfbef4c2405033d7f91521
                var size: Int? = null, // 888
                var url: String? = null // https://piston-data.mojang.com/v1/objects/bd65e7d2e3c237be76cfbef4c2405033d7f91521/client-1.12.xml
            )
        }
    }
}