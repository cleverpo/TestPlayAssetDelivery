import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.android.build.api.variant.VariantSelector
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.api.variant.AndroidComponentsExtension


abstract class CustomPlugin implements Plugin<Project>{
    void apply(Project project){
        project.plugins.withType(AppPlugin.class).each {
            def androidComponents = project.extensions.getByType(ApplicationAndroidComponentsExtension.class)
            androidComponents.onVariants(androidComponents.selector().all(), { Variant variant->
                println variant.buildType
            })
        }
    }
}