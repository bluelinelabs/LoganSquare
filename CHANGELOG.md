## 1.1.0

* Added support for only parsing or only serializing certain fields using the `@JsonIgnore` annotation's new `ignorePolicy` parameter.
* Potentially breaking change: Added support for optionally serializing null fields in an object using the `serializeNullObjects` parameter in the `@JsonObject`. The default is `false` (null fields will not be serialized). LoganSquare 1.0.x always serialized these, so if you're expecting them to be serialized, set this to `true` for your `@JsonObject`s.
* Potentially breaking change: Added support for optionally serializing null elements in a collection or array using the `serializeNullCollectionElements` parameter in the `@JsonObject`. The default is `false` (null fields will not be serialized). LoganSquare 1.0.x always serialized these, so if you're expecting them to be serialized, set this to `true` for your `@JsonObject`s.

## 1.0.4

* Added support for parsing arrays
* Squashed some more bugs

## 1.0.3

* Added support for naming policies, which alleviate the need to add `name` parameters to all of your `@JsonField` annotations if your API follows a certain naming scheme.
* Added support for different field detection policies. The default policy is `ANNOTATIONS_ONLY`, which tells LoganSquare to only parse and serialize fields that have been annotated as `@JsonField`s. Other options are `NONPRIVATE_FIELDS` and `NONPRIVATE_FIELDS_AND_ACCESSORS`, which tell LoganSquare to use all non-private fields and all non-private fields and accessors, respectively, even if they aren't annotated as a `@JsonField`. Fields can be excluded using `@JsonIgnore`.
* Squashed some bugs.

## 1.0.1

* Just simplified some annotations before the general public tried it out.

## 1.0.0

* First release.