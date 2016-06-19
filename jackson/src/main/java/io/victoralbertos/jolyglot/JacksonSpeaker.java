/*
 * Copyright 2016 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.victoralbertos.jolyglot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Jackson implementation of Jolyglot
 */
public class JacksonSpeaker implements JolyglotGenerics {
  private final ObjectMapper mapper;

  public JacksonSpeaker(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public JacksonSpeaker() {
    this.mapper = new ObjectMapper();
    this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override public String toJson(Object src) throws RuntimeException {
    try {
      return mapper.writeValueAsString(src);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override public String toJson(Object src, Type typeOfSrc) {
    try {
      String json = mapper.writeValueAsString(src);
      return json;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(String json, Class<T> classOfT) throws RuntimeException {
    try {
      return mapper.readValue(json, classOfT);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(String json, final Type typeOfT) throws RuntimeException {
    try {
      TypeReference<T> referenceType = new TypeReference<T>(){
        @Override public Type getType() {
          return typeOfT;
        }
      };
      return mapper.readValue(json, referenceType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(File file, Class<T> classOfT) throws RuntimeException {
    try {
      return mapper.readValue(file, classOfT);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(File file, final Type typeOfT) throws RuntimeException {
    try {
      TypeReference<T> referenceType = new TypeReference<T>(){
        @Override public Type getType() {
          return typeOfT;
        }
      };
      T object = mapper.readValue(file, referenceType);
      return object;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override public GenericArrayType arrayOf(Type componentType) {
    return Types.arrayOf(componentType);
  }


  /**
   * {@inheritDoc}
   */
  @Override public ParameterizedType newParameterizedType(Type rawType, Type... typeArguments) {
    return Types.newParameterizedType(rawType, typeArguments);
  }

}
