package com.bluelinelabs.logansquare.processor.processor;

import com.bluelinelabs.logansquare.Constants;
import com.bluelinelabs.logansquare.annotation.JsonEnum;
import com.bluelinelabs.logansquare.processor.JsonEnumHolder;
import com.bluelinelabs.logansquare.processor.JsonObjectHolder;
import com.bluelinelabs.logansquare.processor.TypeUtils;
import com.squareup.javapoet.TypeName;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.bluelinelabs.logansquare.processor.JsonEnumHolder.JsonEnumHolderBuilder;
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
                processJsonEnumAnnotation(element, jsonEnumMap, elements, types);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));

                error(element, "Unable to generate injector for %s. Stack trace incoming:\n%s", JsonEnum.class, stackTrace.toString());
            }
        }
    }

    private void processJsonEnumAnnotation(Element element, Map<String, JsonEnumHolder> jsonEnumMap, Elements elements, Types types) {
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

            Map<String, String> valuesMap = new HashMap<>();
            for (Element enumValue : enumValues) {
                valuesMap.put(enumValue.getSimpleName().toString(), enumValue.getSimpleName().toString().toLowerCase());
            }

            holder = new JsonEnumHolderBuilder()
                    .setPackageName(packageName)
                    .setInjectedClassName(injectedSimpleClassName)
                    .setObjectTypeName(TypeName.get(typeElement.asType()))
                    .setValuesMap(valuesMap)
                    .build();

            jsonEnumMap.put(TypeUtils.getInjectedConverterFQCN(typeElement, elements), holder);
        }
    }

}
