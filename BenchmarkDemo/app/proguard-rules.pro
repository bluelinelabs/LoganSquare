# LoganSquare
-keep class com.bluelinelabs.logansquare.** { *; }
-keep @com.bluelinelabs.logansquare.annotation.JsonObject class *
-keep class **$$JsonObjectMapper { *; }

# Jackson
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry

# DSL-JSON
-keep class dsl_json.json.** { *; }
-keep @com.dslplatform.json.CompiledJson class *
