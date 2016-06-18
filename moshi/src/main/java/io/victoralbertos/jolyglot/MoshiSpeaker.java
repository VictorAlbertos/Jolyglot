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

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import okio.BufferedSource;
import okio.Okio;

/**
 * Moshi implementation of Jolyglot
 */
public class MoshiSpeaker implements Jolyglot {
  private final Moshi moshi;

  public MoshiSpeaker(Moshi moshi) {
    this.moshi = moshi;
  }

  public MoshiSpeaker() {
    this.moshi = new Moshi.Builder().build();
  }

  /**
   * {@inheritDoc}
   */
  @Override public String toJson(Object src) throws RuntimeException {
    JsonAdapter<Object> jsonAdapter = moshi.adapter(Object.class);
    return jsonAdapter.toJson(src);
  }

  /**
   * {@inheritDoc}
   */
  @Override public String toJson(Object src, Type typeOfSrc) {
    JsonAdapter<Object> jsonAdapter = moshi.adapter(typeOfSrc);
    return jsonAdapter.toJson(src);
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(String json, Class<T> classOfT) throws RuntimeException {
    try {
      JsonAdapter<T> jsonAdapter = moshi.adapter(classOfT);
      return jsonAdapter.fromJson(json);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(String json, Type type) throws RuntimeException {
    try {
      JsonAdapter<T> jsonAdapter = moshi.adapter(type);
      return jsonAdapter.fromJson(json);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(File file, Class<T> classOfT) throws RuntimeException {
    try {
      BufferedSource bufferedSource = Okio
          .buffer(Okio.source(file));

      JsonAdapter<T> jsonAdapter = moshi.adapter(classOfT);
      return jsonAdapter.fromJson(JsonReader.of(bufferedSource));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(File file, Type typeOfT) throws RuntimeException {
    try {
      BufferedSource bufferedSource = Okio
          .buffer(Okio.source(file));

      JsonAdapter<T> jsonAdapter = moshi.adapter(typeOfT);
      return jsonAdapter.fromJson(JsonReader.of(bufferedSource));
    } catch (IOException e) {
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
  @Override public ParameterizedType newParameterizedType(Type rawType,
      Type... typeArguments) {
    return Types.newParameterizedType(rawType, typeArguments);
  }

}
