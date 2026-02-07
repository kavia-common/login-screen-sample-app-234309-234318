androidApplication {
    namespace = "org.example.app"

    dependencies {
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation("junit:junit:4.13.2")
        implementation(project(":utilities"))

        // Jetpack Compose
        // NOTE: In this Gradle Declarative DSL sample, BOM alignment may not be applied to
        // version-less artifacts during metadata checks. Use explicit versions to ensure
        // dependencies resolve correctly.
        implementation(platform("androidx.compose:compose-bom:2024.10.01"))

        // Core Compose UI (explicit versions)
        implementation("androidx.compose.runtime:runtime:1.7.6")
        implementation("androidx.compose.ui:ui:1.7.6")
        implementation("androidx.compose.ui:ui-tooling-preview:1.7.6")
        implementation("androidx.compose.material3:material3:1.3.1")

        // Host Compose in an Activity (setContent)
        implementation("androidx.activity:activity-compose:1.9.2")

        // Compose runtime integration helpers
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    }
}
