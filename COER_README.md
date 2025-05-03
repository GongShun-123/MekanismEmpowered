[Mekanism: Empowered README](./README.md)

# Mekanism: Empowered Core

An extension library mod specialized in empowering Mekanism.

Primarily used by Mekanism: Empowered, but compatible with other mods as well.

## Features

### Kotlin-Friendly

- Designed with Kotlin usage in mind.
- Usable in Java, but Kotlin offers additional benefits.

### Enum Extension Support

- Assists with extending enums via Mixin.

### Added Upgrades

- Adds upgrades using enum extension support and Mixin.

### Added WindowType

- Adds WindowType entries using enum extension support and Mixin.

## Usage

Add the following to your `build.gradle.kts`:

### Repository

```kotlin
repositories {
    maven {
        name = "Mekanism: Empowered Core"
        url = uri("https://maven.lapis256.dev")
    }
}
```

### Dependency

```kotlin
dependencies {
    implementation("dev.lapis256:MekanismEmpowered:1.21.1-0.0.2:core")
}
```
