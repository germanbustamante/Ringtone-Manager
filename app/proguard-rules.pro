# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Preserve line numbers for readable crash stack traces, but hide the original
# source file name in the obfuscated build.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Credential Manager https://developer.android.com/identity/sign-in/credential-manager
-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
  *;
}

# kotlinx.serialization — navigation destinations are @Serializable NavKeys.
# Keep generated serializers and the synthetic serializer() accessors so R8
# does not strip them under reflection-free serialization.
-keepattributes *Annotation*, InnerClasses
-keepclassmembers class com.germandebustamante.ringtonemanager.**$$serializer {
    *;
}
-keepclassmembers class com.germandebustamante.ringtonemanager.** {
    *** Companion;
}
-keepclasseswithmembers class com.germandebustamante.ringtonemanager.** {
    kotlinx.serialization.KSerializer serializer(...);
}