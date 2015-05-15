##Model Creation

###Sample Models

LoganSquare provides a few robust ways to create your models. Sample models for each strategy are provided below:

 * [Annotating every field](AnnotationsOnlyModel.md)
  * This is the recommended type of model. It requires writing more annotations, but it's less error prone.
 * [Include all public and package-local fields](PrivateFieldsModel.md)
  * This strategy will assume that all of your public and package-local fields should be parsed and serialized. You'll have to write way less annotations, but errors will occur if you have any fields that can't be converted to or from JSON using a registered `TypeConverter`.
 * [Include all public and package-local field AND accessors](PrivateFieldsAndAccessorsModel.md)
  * This uses the same concept as the above stretegy, but also includes any private fields that have both a getter and a setter.

###Field Naming Policies

By default, LoganSquare assumes that the JSON field name will match your Java variable's name unless the `name` parameter has been used in the field's `@JsonField` annotation. This can be changed by passing another value into the `@JsonObject` annotation's `fieldNamingPolicy` variable. 

Currently the only options are `FIELD_NAME`, which is the default described above, and `LOWER_CASE_WITH_UNDERSCORES`, which will cause LoganSquare to assume that your JSON fields are named the same as your java variable names, except converted to lower case with undercore notation instead of camel case.

###Serializing Null Values

By default, LoganSquare will not serialize `null` values or collection elements into your JSON object. To change this, set the `@JsonObject` annotation's `serializeNullObjects` and/or `serializeNullCollectionElements` to `true`.