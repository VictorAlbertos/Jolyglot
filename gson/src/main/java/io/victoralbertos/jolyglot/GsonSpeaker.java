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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Gson implementation of Jolyglot
 */
public class GsonSpeaker implements JolyglotGenerics {
  private final Gson gson;

  public GsonSpeaker(Gson gson) {
    this.gson = gson;
  }

  public GsonSpeaker() {
    this.gson = new Gson();
  }

  /**
   * {@inheritDoc}
   */
  @Override public String toJson(Object src) throws RuntimeException {
      return gson.toJson(src);
  }

  /**
   * {@inheritDoc}
   */
  @Override public String toJson(Object src, Type typeOfSrc) {
    return gson.toJson(src, typeOfSrc);
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(String json, Class<T> classOfT) throws RuntimeException {
      return gson.fromJson(json, classOfT);
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(String json, Type typeOfT) throws RuntimeException {
      return gson.fromJson(json, typeOfT);
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(File file, Class<T> classOfT) throws RuntimeException {
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
      T object =  gson.fromJson(reader, classOfT);
      reader.close();
      return object;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException i) {}
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(File file, Type typeOfT) throws RuntimeException {
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
      T object =  gson.fromJson(reader, typeOfT);
      reader.close();
      return object;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException i) {}
      }
    }
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

}
