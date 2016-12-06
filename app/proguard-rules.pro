# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Users\uta\Android_SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
-keep class android.support.** { *; }
-keep interface android.support.v7.** { *; }
-keep class * extends java.util.ListResourceBundle {
  protected Object[][] getContents();
}
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
  public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
  @com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
  public static final ** CREATOR;
}
-keep class com.kobakei.** { *; }
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keep class com.activeandroid.** { *; }
-keep class com.activeandroid.**.** { *; }
-keep class * extends com.activeandroid.Model
-keepattributes Column
-keepattributes Table
-keepclasseswithmembers class * {
  @com.activeandroid.annotation.Column <fields>;
}
-keepclassmembers class ** {
   public void onEvent*(**);
}
-keep class org.androidannotations.** { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-dontwarn **
