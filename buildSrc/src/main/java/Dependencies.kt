object Sdk {
    const val MIN_SDK_VERSION = 26
    const val TARGET_SDK_VERSION = 31
    const val COMPILE_SDK_VERSION = 31
}

object BuildPluginsVersion {
    const val GRADLE = "7.0.1"
    const val KOTLIN = "1.5.30"
    const val KTLINT = "9.2.1"
}

object Versions {
    const val APPCOMPAT = "1.1.0"
    const val COMPOSE = "0.1.0-dev13"
    const val CONSTRAINT_LAYOUT = "1.1.3"
    const val COROUTINES = "1.5.1-native-mt"
    const val CORE_KTX = "1.3.0"
    const val JVM = "1.8"
    const val KOTLIN_WRAPPERS_BOM = "0.0.1-pre.238-kotlin-1.5.30"
    const val KOTLIN_REACT_NPM = "17.0.2"
    const val KTOR = "1.6.3"
    const val MATERIAL = "1.3.0-alpha02"
    const val SERIALIZER = "1.2.2"
    const val SQLDELIGHT = "1.5.0"
    // testing dependencies
    const val ANDROIDX_TEST_EXT = "1.1.1"
    const val ANDROIDX_TEST = "1.2.0"
    const val HAMCREST = "1.3"
    const val JUNIT = "4.13"
    const val ESPRESSO_CORE = "3.2.0"
    const val SQLITE_DRIVER = "1.3.0"
    const val TRUTH = "1.0.1"
}

object Plugins {
    const val GRADLE = "com.android.tools.build:gradle:${BuildPluginsVersion.GRADLE}"
    const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${BuildPluginsVersion.KOTLIN}"
}

object Kotlin {
    const val SERIALIZATION_JSON = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.SERIALIZER}"
    const val WRAPPERS = "org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:${Versions.KOTLIN_WRAPPERS_BOM}"
    const val REACT = "org.jetbrains.kotlin-wrappers:kotlin-react"
    const val REACT_DOM = "org.jetbrains.kotlin-wrappers:kotlin-react-dom"
    const val STYLED = "org.jetbrains.kotlin-wrappers:kotlin-styled"
}

object Coroutines {
    const val ANDROID = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
    const val CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
    const val WEB = "org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${Versions.COROUTINES}"
    const val TEST = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}"
}

object Ktor {
    const val ANDROID = "io.ktor:ktor-client-android:${Versions.KTOR}"
    const val CORE = "io.ktor:ktor-client-core:${Versions.KTOR}"
    const val IOS = "io.ktor:ktor-client-ios:${Versions.KTOR}"
    const val SERIALIZATION ="io.ktor:ktor-client-serialization:${Versions.KTOR}"
    const val LOGGING = "io.ktor:ktor-client-logging:${Versions.KTOR}"
    const val WEB = "io.ktor:ktor-client-js:${Versions.KTOR}"
    const val WEB_LOGGING = "io.ktor:ktor-client-logging-js:${Versions.KTOR}"
    // auth
    const val AUTH = "io.ktor:ktor-client-auth:${Versions.KTOR}"
    const val AUTH_JS = "io.ktor:ktor-client-auth-js:${Versions.KTOR}"
    const val AUTH_JVM = "io.ktor:ktor-client-auth-jvm:${Versions.KTOR}"
    const val AUTH_NATIVE = "io.ktor:ktor-client-auth-native:${Versions.KTOR}"
}

object SqlDelight{
    const val GRADLE = "com.squareup.sqldelight:gradle-plugin:${Versions.SQLDELIGHT}"
    const val RUNTIME = "com.squareup.sqldelight:runtime:${Versions.SQLDELIGHT}"
    const val RUNTIME_JDK = "com.squareup.sqldelight:runtime-jvm:${Versions.SQLDELIGHT}"
    const val RUNTIME_DRIVER_COMMON = "com.squareup.sqldelight:sqlite-driver:${Versions.SQLDELIGHT}"
    const val RUNTIME_DRIVER_IOS = "com.squareup.sqldelight:native-driver:${Versions.SQLDELIGHT}"
    const val RUNTIME_DRIVER_ANDROID = "com.squareup.sqldelight:android-driver:${Versions.SQLDELIGHT}"
    const val SQLITE_TEST_DRIVER = "com.squareup.sqldelight:sqlite-driver:${Versions.SQLITE_DRIVER}"
    const val RUNTIME_DRIVER_JS = "com.squareup.sqldelight:runtime-js:${Versions.SQLDELIGHT}"
}

object SupportLibs {
    const val ANDROIDX_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val ANDROIDX_CONSTRAINT_LAYOUT = "com.android.support.constraint:constraint-layout:${Versions.CONSTRAINT_LAYOUT}"
    const val ANDROIDX_CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val GOOGLE_MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
}

object TestingLib {
    const val HAMCREST = "org.hamcrest:hamcrest-library:${Versions.HAMCREST}"
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
    const val TRUTH = "com.google.truth:truth:${Versions.TRUTH}"
}

object AndroidTestingLib {
    const val ANDROIDX_TEST_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_EXT}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
}