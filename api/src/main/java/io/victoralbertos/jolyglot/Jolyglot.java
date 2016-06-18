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

import java.io.File;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * A layer of abstraction for common data binding operations
 * built upon next popular json libraries: Gson, Jackson and Moshi.
 */
public interface Jolyglot {
  /**
   * This method serializes the specified object into its equivalent Json representation.
   * This method should be used when the specified object is not a generic type.
   * @param src the object for which Json representation is to be created setting for Gson
   * @return Json representation of {@code src}.
   */
  String toJson(Object src) throws RuntimeException;

  /**
   * This method serializes the specified object, including those of generic types, into its
   * equivalent Json representation. This method must be used if the specified object is a generic
   * type. For non-generic objects, use {@link #toJson(Object)} instead.
   * @param src the object for which JSON representation is to be created
   * @param typeOfSrc The specific genericized type of src.
   * @return Json representation of {@code src}
   */

  String toJson(Object src, Type typeOfSrc);
  /**
   * This method deserializes the specified Json into an object of the specified class. It is not
   * suitable to use if the specified class is a generic type since it will not have the generic
   * type information because of the Type Erasure feature of Java. Therefore, this method should not
   * be used if the desired type is a generic type. Note that this method works fine if the any of
   * the fields of the specified object are generics, just the object itself should not be a
   * generic type. For the cases when the object is of generic type, invoke
   * {@link #fromJson(String, Type)}. If you have the Json in a {@link File} instead of
   * a String, use {@link #fromJson(File, Class)} instead.
   * @param <T> the type of the desired object
   * @param json the string from which the object is to be deserialized
   * @param classOfT the class of T
   * @return an object of type T from the string.
   */
  <T> T fromJson(String json, Class<T> classOfT) throws RuntimeException;

  /**
   * This method deserializes the specified Json into an object of the specified type. This method
   * is useful if the specified object is a generic type. For non-generic objects, use
   * {@link #fromJson(String, Class)} instead. If you have the Json in a {@link File} instead of
   * a String, use {@link #fromJson(File, Type)} instead.
   */
  <T> T fromJson(String json, Type type) throws RuntimeException;

  /**
   * This method deserializes the Json read from the specified reader into an object of the
   * specified class. It is not suitable to use if the specified class is a generic type since it
   * will not have the generic type information because of the Type Erasure feature of Java.
   * Therefore, this method should not be used if the desired type is a generic type. Note that
   * this method works fine if the any of the fields of the specified object are generics, just the
   * object itself should not be a generic type. For the cases when the object is of generic type,
   * invoke {@link #fromJson(File, Type)}. If you have the Json in a String form instead of a
   * {@link File}, use {@link #fromJson(String, Class)} instead.
   * @param <T> the type of the desired object
   * @param file the file producing the Json from which the object is to be deserialized.
   * @param classOfT the class of T
   * @return an object of type T from the string.
   */
  <T> T fromJson(File file, Class<T> classOfT) throws RuntimeException;

  /**
   * This method deserializes the Json read from the specified reader into an object of the
   * specified type. This method is useful if the specified object is a generic type. For
   * non-generic objects, use {@link #fromJson(File, Class)} instead. If you have the Json in a
   * String form instead of a {@link File}, use {@link #fromJson(String, Type)} instead.
   * @param <T> the type of the desired object
   * @param file the file producing Json from which the object is to be deserialized
   * @param typeOfT The specific genericized type of src.
   * @return an object of type T from the json.
   */
  <T> T fromJson(File file, Type typeOfT) throws RuntimeException;

  /** Returns an array type whose elements are all instances of {@code componentType}. */
  GenericArrayType arrayOf(Type componentType);

  /**
   * Returns a new parameterized type, applying {@code typeArguments} to {@code rawType}.
   */
  ParameterizedType newParameterizedType(Type rawType, Type... typeArguments);
}
