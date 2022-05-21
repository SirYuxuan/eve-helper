package com.yuxuan66.config.system;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author Sir丶雨轩
 * @since 2021/6/24
 */
@Configuration
public class JacksonConfig {

    private static final String DEFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";

    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        serializerProvider.setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
                jsonGenerator.writeString("");
            }

        });
        // StringSerializer stringSerializer = new StringSerializer();
        SimpleModule simpleModule = new SimpleModule();
        //simpleModule.addSerializer(String.class,stringSerializer);

        // 这个设置会覆盖字段注解
        objectMapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));

        //设置全局日期格式同时允许 DateTimeFormat 注解,默认取JsonFormat
        //其次取DateTimeFormat,都取不到用默认的
        objectMapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            public Object findSerializer(Annotated a) {
                if(a instanceof AnnotatedMethod) {
                    AnnotatedElement m=a.getAnnotated();
                    JsonFormat an=m.getAnnotation(JsonFormat.class);
                    DateTimeFormat dtm=m.getAnnotation(DateTimeFormat.class);
                    if(an!=null) {
                        if(!DEFAULT_DATE_FORMAT.equals(an.pattern())) {
                            return new TimestampSerializer(an.pattern());
                        }
                    }else if(dtm!=null){
                        if(!DEFAULT_DATE_FORMAT.equals(dtm.pattern())) {
                            return new TimestampSerializer(dtm.pattern());
                        }
                    }
                }
                return super.findSerializer(a);
            }
        });

        simpleModule.addSerializer(Timestamp.class, new TimestampSerializer(DEFAULT_DATE_FORMAT));
        objectMapper.registerModule(simpleModule);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true) ;
        return objectMapper;
    }

    static class TimestampSerializer extends JsonSerializer<Timestamp> {

        private String pattern;

        public TimestampSerializer(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(DateUtil.format(value,pattern));
        }
    }
}
