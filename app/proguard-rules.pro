# Add project specific ProGuard rules here.
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.ftt.signal.data.model.** { *; }
