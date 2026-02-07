androidApplication {
    namespace = "org.example.app"

    dependencies {
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation("junit:junit:4.13.2")
        implementation(project(":utilities"))

        // Jetpack Compose (used via ComposeView fallback so we don't need to add new Gradle blocks in DCL)
        implementation("androidx.activity:activity-compose:1.9.3")
        implementation("androidx.compose.ui:ui:1.7.6")
        implementation("androidx.compose.ui:ui-tooling-preview:1.7.6")
        implementation("androidx.compose.material3:material3:1.3.1")
        implementation("androidx.compose.ui:ui-text:1.7.6")
        implementation("androidx.compose.ui:ui-unit:1.7.6")
    }
}
