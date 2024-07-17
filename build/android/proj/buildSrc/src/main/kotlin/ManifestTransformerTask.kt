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

        //替换admob MobileAdsInitProviderProxy
        manifest = manifest.replace("com.google.android.gms.ads.MobileAdsInitProvider", "com.quik.testpad.dagger.dynamicfeature.modules.ad.MobileAdsInitProviderProxy")

        //替换facebook FacebookInitProvider
        manifest = manifest.replace("com.facebook.internal.FacebookInitProvider", "com.quik.testpad.dagger.dynamicfeature.modules.facebook.FacebookInitProviderProxy")

        //替换facebook AudienceNetworkContentProviderProxy
        manifest = manifest.replace("com.facebook.ads.AudienceNetworkContentProvider", "com.quik.testpad.dagger.dynamicfeature.modules.facebook.AudienceNetworkContentProviderProxy")

        println("Writes to " + updatedManifest.get().asFile.getAbsolutePath() + "\n New Content:" + manifest)
        updatedManifest.get().asFile.writeText(manifest)
    }
}
