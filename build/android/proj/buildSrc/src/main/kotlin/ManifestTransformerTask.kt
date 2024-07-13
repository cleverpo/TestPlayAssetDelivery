import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class ManifestTransformerTask: DefaultTask() {

    @get:InputFile
    abstract val mergedManifest: RegularFileProperty

    @get:OutputFile
    abstract val updatedManifest: RegularFileProperty

    @TaskAction
    fun taskAction() {
        var manifest = mergedManifest.asFile.get().readText()
        manifest = manifest.replace("com.google.android.gms.ads.MobileAdsInitProvider", "com.quik.testpad.MobileAdsInitProviderProxy")
        println("Writes to " + updatedManifest.get().asFile.getAbsolutePath() + "\n New Content:" + manifest)
        updatedManifest.get().asFile.writeText(manifest)
    }
}
