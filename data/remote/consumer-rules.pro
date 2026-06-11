# Consumer rules for :data:remote — applied to any app module that depends on it.

# Firestore deserializes documents into DTOs via reflection (toObject<T>()),
# matching field names. R8 must not rename or strip these classes, their
# fields or their no-arg constructors.
-keep class com.germandebustamante.ringtonemanager.data.remote.model.** { *; }
-keepclassmembers class com.germandebustamante.ringtonemanager.data.remote.model.** {
    <init>();
    <fields>;
}
