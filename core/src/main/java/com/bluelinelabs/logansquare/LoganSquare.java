package com.bluelinelabs.logansquare;

import com.bluelinelabs.logansquare.internal.JsonMapperLoader;
import com.bluelinelabs.logansquare.typeconverters.DefaultCalendarConverter;
import com.bluelinelabs.logansquare.typeconverters.DefaultDateConverter;
import com.bluelinelabs.logansquare.typeconverters.TypeConverter;
import com.bluelinelabs.logansquare.util.SimpleArrayMap;
import com.fasterxml.jackson.core.JsonFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/** The point of all interaction with this library. */
public class LoganSquare {

    private static final SimpleArrayMap<Class, JsonMapper> OBJECT_MAPPERS = new SimpleArrayMap<>();

    private static CopyOnWriteArrayList<JsonMapperLoader> JSON_MAPPER_LOADERS;
    static {
        JSON_MAPPER_LOADERS = new CopyOnWriteArrayList<>();
        for (JsonMapperLoader loader : ServiceLoader.load(JsonMapperLoader.class)) {
            JSON_MAPPER_LOADERS.add(loader);
            loader.putAllJsonMappers(OBJECT_MAPPERS);
        }
        try {
            JsonMapperLoader loader = (JsonMapperLoader)Class.forName(Constants.LOADER_PACKAGE_NAME + "." + Constants.LOADER_CLASS_NAME).newInstance();
            loader.putAllJsonMappers(OBJECT_MAPPERS);
            JSON_MAPPER_LOADERS.add(loader);
        } catch (Exception e) {
            // If no JsonMapperLoader service found, and default Loader is not found, throw exception
            if (JSON_MAPPER_LOADERS.isEmpty()) {
                throw new RuntimeException("JsonMapperLoaderImpl class not found. Have you included the steps needed for LoganSquare to process your annotations?");
            }
        }
    }

    private static final SimpleArrayMap<Class, TypeConverter> TYPE_CONVERTERS = new SimpleArrayMap<>();
    static {
        registerTypeConverter(Date.class, new DefaultDateConverter());
        registerTypeConverter(Calendar.class, new DefaultCalendarConverter());
    }

    /** The JsonFactory that should be used throughout the entire app. */
    public static final JsonFactory JSON_FACTORY = new JsonFactory();

    /**
     * Parse an object from an InputStream.
     *
     * @param is The InputStream, most likely from your networking library.
     * @param jsonObjectClass The @JsonObject class to parse the InputStream into
     */
    public static <E> E parse(InputStream is, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).parse(is);
    }

    /**
     * Parse an object from a String. Note: parsing from an InputStream should be preferred over parsing from a String if possible.
     *
     * @param jsonString The JSON string being parsed.
     * @param jsonObjectClass The @JsonObject class to parse the InputStream into
     */
    public static <E> E parse(String jsonString, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).parse(jsonString);
    }

    /**
     * Parse a parameterized object from an InputStream.
     *
     * @param is The InputStream, most likely from your networking library.
     * @param jsonObjectType The ParameterizedType describing the object. Ex: LoganSquare.parse(is, new ParameterizedType&lt;MyModel&lt;OtherModel&gt;&gt;() { });
     */
    public static <E> E parse(InputStream is, ParameterizedType<E> jsonObjectType) throws IOException {
        return mapperFor(jsonObjectType).parse(is);
    }

    /**
     * Parse a parameterized object from a String. Note: parsing from an InputStream should be preferred over parsing from a String if possible.
     *
     * @param jsonString The JSON string being parsed.
     * @param jsonObjectType The ParameterizedType describing the object. Ex: LoganSquare.parse(is, new ParameterizedType&lt;MyModel&lt;OtherModel&gt;&gt;() { });
     */
    public static <E> E parse(String jsonString, ParameterizedType<E> jsonObjectType) throws IOException {
        return mapperFor(jsonObjectType).parse(jsonString);
    }

    /**
     * Parse a list of objects from an InputStream.
     *
     * @param is The inputStream, most likely from your networking library.
     * @param jsonObjectClass The @JsonObject class to parse the InputStream into
     */
    public static <E> List<E> parseList(InputStream is, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).parseList(is);
    }

    /**
     * Parse a list of objects from a String. Note: parsing from an InputStream should be preferred over parsing from a String if possible.
     *
     * @param jsonString The JSON string being parsed.
     * @param jsonObjectClass The @JsonObject class to parse the InputStream into
     */
    public static <E> List<E> parseList(String jsonString, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).parseList(jsonString);
    }

    /**
     * Parse a map of objects from an InputStream.
     *
     * @param is The inputStream, most likely from your networking library.
     * @param jsonObjectClass The @JsonObject class to parse the InputStream into
     */
    public static <E> Map<String, E> parseMap(InputStream is, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).parseMap(is);
    }

    /**
     * Parse a map of objects from a String. Note: parsing from an InputStream should be preferred over parsing from a String if possible.
     *
     * @param jsonString The JSON string being parsed.
     * @param jsonObjectClass The @JsonObject class to parse the InputStream into
     */
    public static <E> Map<String, E> parseMap(String jsonString, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).parseMap(jsonString);
    }

    /**
     * Serialize an object to a JSON String.
     *
     * @param object The object to serialize.
     */
    @SuppressWarnings("unchecked")
    public static <E> String serialize(E object) throws IOException {
        return mapperFor((Class<E>)object.getClass()).serialize(object);
    }

    /**
     * Serialize an object to an OutputStream.
     *
     * @param object The object to serialize.
     * @param os The OutputStream being written to.
     */
    @SuppressWarnings("unchecked")
    public static <E> void serialize(E object, OutputStream os) throws IOException {
        mapperFor((Class<E>)object.getClass()).serialize(object, os);
    }

    /**
     * Serialize a parameterized object to a JSON String.
     *
     * @param object The object to serialize.
     * @param parameterizedType The ParameterizedType describing the object. Ex: LoganSquare.serialize(object, new ParameterizedType&lt;MyModel&lt;OtherModel&gt;&gt;() { });
     */
    @SuppressWarnings("unchecked")
    public static <E> String serialize(E object, ParameterizedType<E> parameterizedType) throws IOException {
        return mapperFor(parameterizedType).serialize(object);
    }

    /**
     * Serialize a parameterized  object to an OutputStream.
     *
     * @param object The object to serialize.
     * @param parameterizedType The ParameterizedType describing the object. Ex: LoganSquare.serialize(object, new ParameterizedType&lt;MyModel&lt;OtherModel&gt;&gt;() { }, os);
     * @param os The OutputStream being written to.
     */
    @SuppressWarnings("unchecked")
    public static <E> void serialize(E object, ParameterizedType<E> parameterizedType, OutputStream os) throws IOException {
        mapperFor(parameterizedType).serialize(object, os);
    }

    /**
     * Serialize a list of objects to a JSON String.
     *
     * @param list The list of objects to serialize.
     * @param jsonObjectClass The @JsonObject class of the list elements
     */
    public static <E> String serialize(List<E> list, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).serialize(list);
    }

    /**
     * Serialize a list of objects to an OutputStream.
     *
     * @param list The list of objects to serialize.
     * @param os The OutputStream to which the list should be serialized
     * @param jsonObjectClass The @JsonObject class of the list elements
     */
    public static <E> void serialize(List<E> list, OutputStream os, Class<E> jsonObjectClass) throws IOException {
        mapperFor(jsonObjectClass).serialize(list, os);
    }

    /**
     * Serialize a map of objects to a JSON String.
     *
     * @param map The map of objects to serialize.
     * @param jsonObjectClass The @JsonObject class of the list elements
     */
    public static <E> String serialize(Map<String, E> map, Class<E> jsonObjectClass) throws IOException {
        return mapperFor(jsonObjectClass).serialize(map);
    }

    /**
     * Serialize a map of objects to an OutputStream.
     *
     * @param map The map of objects to serialize.
     * @param os The OutputStream to which the list should be serialized
     * @param jsonObjectClass The @JsonObject class of the list elements
     */
    public static <E> void serialize(Map<String, E> map, OutputStream os, Class<E> jsonObjectClass) throws IOException {
        mapperFor(jsonObjectClass).serialize(map, os);
    }

    @SuppressWarnings("unchecked")
    /*package*/ static <E> JsonMapper<E> getMapper(Class<E> cls) {
        JsonMapper<E> mapper = OBJECT_MAPPERS.get(cls);
        if (mapper == null) {
            // The only way the mapper wouldn't already be loaded into OBJECT_MAPPERS is if it was compiled separately, but let's handle it anyway
            try {
                Class<?> mapperClass = Class.forName(cls.getName() + Constants.MAPPER_CLASS_SUFFIX);
                mapper = (JsonMapper<E>)mapperClass.newInstance();
                OBJECT_MAPPERS.put(cls, mapper);
            } catch (Exception ignored) { }
        }
        return mapper;
    }

    @SuppressWarnings("unchecked")
    private static <E> JsonMapper<E> getMapper(ParameterizedType<E> type, SimpleArrayMap<ParameterizedType, JsonMapper> partialMappers) {
        for (JsonMapperLoader loader : JSON_MAPPER_LOADERS) {
            final JsonMapper<E> mapper = loader.mapperFor(type, partialMappers);
            if (mapper != null) return mapper;
        }
        return null;
    }

    /**
     * Returns whether or not LoganSquare can handle a given class.
     *
     * @param cls The class for which support is being checked.
     */
    @SuppressWarnings("unchecked")
    public static boolean supports(Class cls) {
        return getMapper(cls) != null;
    }

    /**
     * Returns whether or not LoganSquare can handle a given ParameterizedType.
     *
     * @param type The ParameterizedType for which support is being checked.
     */
    @SuppressWarnings("unchecked")
    public static boolean supports(ParameterizedType type) {
        return getMapper(type, null) != null;
    }

    /**
     * Returns a JsonMapper for a given class that has been annotated with @JsonObject.
     *
     * @param cls The class for which the JsonMapper should be fetched.
     */
    public static <E> JsonMapper<E> mapperFor(Class<E> cls) throws NoSuchMapperException {
        JsonMapper<E> mapper = getMapper(cls);

        if (mapper == null) {
            throw new NoSuchMapperException(cls);
        } else {
            return mapper;
        }
    }

    /**
     * Returns a JsonMapper for a given class that has been annotated with @JsonObject.
     *
     * @param type The ParameterizedType for which the JsonMapper should be fetched.
     */
    @SuppressWarnings("unchecked")
    public static <E> JsonMapper<E> mapperFor(ParameterizedType<E> type) throws NoSuchMapperException {
        return mapperFor(type, null);
    }

    public static <E> JsonMapper<E> mapperFor(ParameterizedType<E> type, SimpleArrayMap<ParameterizedType, JsonMapper> partialMappers) throws NoSuchMapperException {
        JsonMapper<E> mapper = getMapper(type, partialMappers);
        if (mapper == null) {
            throw new NoSuchMapperException(type.rawType);
        } else {
            return mapper;
        }
    }

    /**
     * Returns a TypeConverter for a given class.
     *
     * @param cls The class for which the TypeConverter should be fetched.
     */
    @SuppressWarnings("unchecked")
    public static <E> TypeConverter<E> typeConverterFor(Class<E> cls) throws NoSuchTypeConverterException {
        TypeConverter<E> typeConverter = TYPE_CONVERTERS.get(cls);
        if (typeConverter == null) {
            throw new NoSuchTypeConverterException(cls);
        }
        return typeConverter;
    }

    /**
     * Register a new TypeConverter for parsing and serialization.
     *
     * @param cls The class for which the TypeConverter should be used.
     * @param converter The TypeConverter
     */
    public static <E> void registerTypeConverter(Class<E> cls, TypeConverter<E> converter) {
        TYPE_CONVERTERS.put(cls, converter);
    }
}
