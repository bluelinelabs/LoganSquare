package com.bluelinelabs.logansquare;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

public abstract class ParameterizedType<T> {
    public final Class rawType;
    public final List<ParameterizedType> typeParameters;

    public ParameterizedType() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("ParameterizedType objects must be instantiated with a type parameter. Ex: new ParameterizedType<MyModel<MyOtherModel>>() { }");
        }

        Type type = ((java.lang.reflect.ParameterizedType)superclass).getActualTypeArguments()[0];

        rawType = getRawType(type);
        typeParameters = new ArrayList<>();
        addTypeParameters(type);
    }

    private ParameterizedType(Type type) {
        rawType = getRawType(type);
        typeParameters = new ArrayList<>();
        addTypeParameters(type);
    }

    private void addTypeParameters(Type type) {
        if (type instanceof java.lang.reflect.ParameterizedType) {
            Type[] actualTypeArguments = ((java.lang.reflect.ParameterizedType)type).getActualTypeArguments();
            if (actualTypeArguments != null) {
                for (Type typeArgument : actualTypeArguments) {
                    typeParameters.add(new ConcreteParameterizedType(typeArgument));
                }
            }
        }
    }

    private Class getRawType(Type type) {
        if (type instanceof Class) {
            return (Class)type;
        } else if (type instanceof java.lang.reflect.ParameterizedType) {
            return (Class)(((java.lang.reflect.ParameterizedType)type).getRawType());
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType)type).getUpperBounds()[0]);
        } else if (type instanceof GenericArrayType) {
            return Array.newInstance(getRawType(((GenericArrayType)type).getGenericComponentType()), 0).getClass();
        } else {
            throw new RuntimeException("Invalid type passed: " + type);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        } else {
            ParameterizedType<?> that = (ParameterizedType<?>)o;

            if (!rawType.equals(that.rawType)) {
                return false;
            }
            return typeParameters.equals(that.typeParameters);
        }
    }

    @Override
    public int hashCode() {
        int result = rawType.hashCode();
        result = 31 * result + typeParameters.hashCode();
        return result;
    }

    static class ConcreteParameterizedType<T> extends ParameterizedType<T> {
        public ConcreteParameterizedType(Type type) {
            super(type);
        }
    }
}