package com.bluelinelabs.logansquare.processor.processor;

import com.bluelinelabs.logansquare.Constants;
import com.bluelinelabs.logansquare.annotation.JsonBooleanValue;
import com.bluelinelabs.logansquare.annotation.JsonEnum;
import com.bluelinelabs.logansquare.annotation.JsonNullValue;
import com.bluelinelabs.logansquare.annotation.JsonNumberValue;
import com.bluelinelabs.logansquare.annotation.JsonStringValue;
import com.bluelinelabs.logansquare.processor.JsonEnumHolder;
import com.bluelinelabs.logansquare.processor.JsonObjectHolder;
import com.bluelinelabs.logansquare.processor.TextUtils;
import com.bluelinelabs.logansquare.processor.TypeUtils;
import com.squareup.javapoet.TypeName;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import javafx.util.Pair;

import static com.bluelinelabs.logansquare.processor.JsonEnumHolder.JsonEnumHolderBuilder;
import static com.bluelinelabs.logansquare.processor.JsonEnumHolder.ValueType;
import static javax.lang.model.element.Modifier.PRIVATE;

public class JsonEnumProcessor extends Processor {

    public JsonEnumProcessor(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public Class getAnnotation() {
        return JsonEnum.class;
    }

    @Override
    public void findAndParseObjects(RoundEnvironment env, Map<String, JsonObjectHolder> jsonObjectMap, Map<String, JsonEnumHolder> jsonEnumMap, Elements elements, Types types) {
        for (Element element : env.getElementsAnnotatedWith(JsonEnum.class)) {
            try {
                processJsonEnumAnnotation(element, jsonEnumMap, elements);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));

                error(element, "Unable to generate injector for %s. Stack trace incoming:\n%s", JsonEnum.class, stackTrace.toString());
            }
        }
    }

    private void processJsonEnumAnnotation(Element element, Map<String, JsonEnumHolder> jsonEnumMap, Elements elements) {
        TypeElement typeElement = (TypeElement) element;
        final List<Element> enumValues = TypeUtils.getEnumValues(element);

        if (element.getModifiers().contains(PRIVATE)) {
            error(element, "%s: %s annotation can't be used on private enums.", typeElement.getQualifiedName(), JsonEnum.class.getSimpleName());
        }
        if (enumValues.isEmpty()) {
            error(element, "%s: %s annotation can't be used on enums with no values.", typeElement.getQualifiedName(), JsonEnum.class.getSimpleName());
        }

        JsonEnumHolder holder = jsonEnumMap.get(TypeUtils.getInjectedConverterFQCN(typeElement, elements));
        if (holder == null) {
            String packageName = elements.getPackageOf(typeElement).getQualifiedName().toString();
            String objectClassName = TypeUtils.getSimpleClassName(typeElement, packageName);
            String injectedSimpleClassName = objectClassName + Constants.CONVERTER_CLASS_SUFFIX;

            JsonEnum annotation = element.getAnnotation(JsonEnum.class);

            Map<String, Object> valuesMap = new HashMap<>();
            ValueType valueType = null;
            for (Element enumValue : enumValues) {
                Pair<ValueType, Object> valueInfo = extractObjectFromEnumValue(enumValue, annotation.valueNamingPolicy());
                if (valueInfo.getKey() != null) {
                    if (valueType == null) {
                        valueType = valueInfo.getKey();
                    }
                    if (valueInfo.getKey() != valueType) {
                        error(element, "%s: %s enum value type %s at %s conflicts with previously declared type %s.", typeElement.getQualifiedName(), valueInfo.getKey(), enumValue.getSimpleName(), valueType);
                    }
                }
                valuesMap.put(enumValue.getSimpleName().toString(), valueInfo.getValue());
            }
            checkValuesForDuplicates(element, typeElement, valuesMap);
            if (valueType == null) {
                error(element, "%s: can't recognize type of values (only null value).", typeElement.getQualifiedName());
            }

            holder = new JsonEnumHolderBuilder()
                    .setPackageName(packageName)
                    .setInjectedClassName(injectedSimpleClassName)
                    .setObjectTypeName(TypeName.get(typeElement.asType()))
                    .setValuesMap(valueType, valuesMap)
                    .build();

            jsonEnumMap.put(TypeUtils.getInjectedConverterFQCN(typeElement, elements), holder);
        }
    }

    private Pair<ValueType, Object> extractObjectFromEnumValue(Element enumValue, JsonEnum.ValueNamingPolicy valueNamingPolicy) {
        JsonStringValue stringAnnotation = enumValue.getAnnotation(JsonStringValue.class);
        if (stringAnnotation != null) {
            return new Pair<ValueType, Object>(ValueType.STRING, stringAnnotation.value());
        }
        JsonNumberValue numberAnnotation = enumValue.getAnnotation(JsonNumberValue.class);
        if (numberAnnotation != null) {
            return new Pair<ValueType, Object>(ValueType.NUMBER, numberAnnotation.value());
        }
        JsonBooleanValue booleanAnnotation = enumValue.getAnnotation(JsonBooleanValue.class);
        if (booleanAnnotation != null) {
            return new Pair<ValueType, Object>(ValueType.BOOLEAN, booleanAnnotation.value());
        }
        if (enumValue.getAnnotation(JsonNullValue.class) != null) {
            return new Pair<ValueType, Object>(null, null);
        }
        switch (valueNamingPolicy){
            case VALUE_NAME:
                return new Pair<ValueType, Object>(ValueType.STRING, enumValue.getSimpleName().toString());
            case LOWER_CASE_WITH_UNDERSCORES:
                return new Pair<ValueType, Object>(ValueType.STRING, TextUtils.toLowerCaseWithUnderscores(enumValue.getSimpleName().toString()));
            default:
                throw new IllegalStateException("Unknown valueNamingPolicy: " + valueNamingPolicy);
        }
    }

    private void checkValuesForDuplicates(Element element, TypeElement typeElement, Map<String, Object> valuesMap) {
        ArrayList<Object> values = new ArrayList<>(valuesMap.values());
        for (int i = 0; i < values.size(); i++) {
            for (int j = i + 1; j < values.size(); j++) {
                Object firstValue = values.get(i);
                Object secondValue = values.get(j);
                if (firstValue == secondValue || (firstValue != null && firstValue.equals(secondValue))) {
                    error(element, "%s contains duplicate values: %s.", typeElement.getQualifiedName(), firstValue);
                }
            }
        }
    }

}
