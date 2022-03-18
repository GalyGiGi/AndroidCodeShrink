package cn.com.lbb.log_gradle_plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MyLogPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        AppExtension appExtension = target.getExtensions().findByType(AppExtension.class);
        LogDeleteExtension extension = target.getExtensions().create("log_delete", LogDeleteExtension.class);

        appExtension.registerTransform(new ZCTransform(target, extension));

    }
}
