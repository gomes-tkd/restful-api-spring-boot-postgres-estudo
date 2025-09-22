package com.github.gomestkd.startup.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ObjectMapper {
    private static final Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    private ObjectMapper() {
        throw new UnsupportedOperationException("Utility class - cannot be instantiated");
    }

    public static <O, D> D parseObject(O objectOrigin, Class<D> destinationClass) {
        return (objectOrigin == null) ? null : mapper.map(objectOrigin, destinationClass);
    }

    public static <O, D> Set<D> parseListObject(Set<O> objectOrigin, Class<D> destinationClass) {
        if (objectOrigin == null || objectOrigin.isEmpty()) {
            return Set.of();
        }

        return objectOrigin.stream()
                .filter(Objects::nonNull)
                .map(source -> mapper.map(source, destinationClass))
                .collect(Collectors.toUnmodifiableSet());
    }
}
