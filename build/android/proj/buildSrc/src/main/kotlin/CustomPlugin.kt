import com.android.build.api.artifact.SingleArtifact
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import com.android.build.api.variant.AndroidComponentsExtension

abstract class CustomPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)

        androidComponents.onVariants { variant ->

            val manifestUpdater =
                project.tasks.register(variant.name + "ManifestUpdater", ManifestTransformerTask::class.java)
            variant.artifacts.use(manifestUpdater)
                .wiredWithFiles(
                    ManifestTransformerTask::mergedManifest,
                    ManifestTransformerTask::updatedManifest)
                .toTransform(SingleArtifact.MERGED_MANIFEST)
        }
    }
}