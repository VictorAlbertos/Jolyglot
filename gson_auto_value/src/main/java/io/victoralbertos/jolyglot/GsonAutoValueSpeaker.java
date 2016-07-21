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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import java.io.File;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Gson implementation of Jolyglot
 */
public abstract class GsonAutoValueSpeaker implements JolyglotGenerics {
  private final GsonSpeaker gsonSpeaker;

  public GsonAutoValueSpeaker() {
    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(autoValueGsonTypeAdapterFactory())
        .create();
    gsonSpeaker = new GsonSpeaker(gson);
  }

  /**
   * {@inheritDoc}
   */
  @Override public String toJson(Object src) {
      return gsonSpeaker.toJson(src);
  }

  /**
   * {@inheritDoc}
   */
  @Override public String toJson(Object src, Type typeOfSrc) {
    return gsonSpeaker.toJson(src, typeOfSrc);
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(String json, Class<T> classOfT) throws RuntimeException {
      return gsonSpeaker.fromJson(json, classOfT);
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(String json, Type typeOfT) throws RuntimeException {
      return gsonSpeaker.fromJson(json, typeOfT);
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(File file, Class<T> classOfT) throws RuntimeException {
    return gsonSpeaker.fromJson(file, classOfT);
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(File file, Type typeOfT) throws RuntimeException {
    return gsonSpeaker.fromJson(file, typeOfT);
  }

  @Override public GenericArrayType arrayOf(Type componentType) {
      return Types.arrayOf(componentType);
  }

  /**
   * {@inheritDoc}
   */
  @Override public ParameterizedType newParameterizedType(Type rawType, Type... typeArguments) {
      return Types.newParameterizedType(rawType, typeArguments);
  }

  protected abstract TypeAdapterFactory autoValueGsonTypeAdapterFactory();

}
