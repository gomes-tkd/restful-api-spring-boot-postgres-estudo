package com.github.gomestkd.startup.serialization.converter;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

public final class YamlJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    private static final MediaType MEDIA_TYPE_YAML = MediaType.valueOf("application/yaml");

    public YamlJackson2HttpMessageConverter() {
        super(
                new YAMLMapper()
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .findAndRegisterModules(),
                MEDIA_TYPE_YAML
        );
    }
}