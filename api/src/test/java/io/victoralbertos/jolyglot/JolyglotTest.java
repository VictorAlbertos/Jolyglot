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
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Base test to be extended from every json provider.
 */
public abstract class JolyglotTest {
  private Jolyglot jolyglot;
  @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Before public void setUp() {
    jolyglot = jolyglot();
  }

  @Test public void toJson() {
    Mock mock = new Mock();
    String jsonMock = jolyglot.toJson(mock);
    assertThat(jsonMock, is(jsonMockSample()));
  }

  @Test public void toJsonType() throws NoSuchMethodException {
    Method method = Types.class.getDeclaredMethod("mockParameterized");
    Type type = method.getGenericReturnType();

    Mock mock = new Mock();
    MockParameterized<Mock> mockParameterized = new MockParameterized<>(mock);
    String jsonMockParameterized = jolyglot.toJson(mockParameterized, type);

    try {
      assertThat(jsonMockParameterized, is(jsonMockParameterizedSample()));
    } catch (AssertionError i) {
      assertThat(jsonMockParameterized, is(jsonMockParameterizedSampleReverse()));
    }
  }

  @Test public void fromStringJsonClass() {
    Mock mock = jolyglot.fromJson(jsonMockSample(), Mock.class);
    assertThat(jolyglot.toJson(mock), is(jsonMockSample()));
  }

  @Test public void fromStringJsonType() throws NoSuchMethodException {
    Method method = Types.class.getDeclaredMethod("mockParameterized");
    Type type = method.getGenericReturnType();

    MockParameterized<Mock> mockParameterized = jolyglot.fromJson(jsonMockParameterizedSample(), type);

    try {
      assertThat(jolyglot.toJson(mockParameterized, type),
          is(jsonMockParameterizedSample()));
    } catch (AssertionError i) {
      assertThat(jolyglot.toJson(mockParameterized, type),
          is(jsonMockParameterizedSampleReverse()));
    }
  }

  @Test public void fromStringPartialJsonType() throws NoSuchMethodException {
    Type type = jolyglot.newParameterizedType(MockParameterized.class, Object.class);
    MockParameterized mockParameterized = jolyglot.fromJson(jsonMockParameterizedSample(), type);

    try {
      assertThat(jolyglot.toJson(mockParameterized, type),
          is(jsonMockParameterizedSample()));
    } catch (AssertionError i) {
      assertThat(jolyglot.toJson(mockParameterized, type),
          is(jsonMockParameterizedSampleReverse()));
    }
  }

  @Test public void fromFileJsonClass() throws IOException {
    File file = temporaryFolder.newFile("test.txt");
    FileWriter printWriter = new FileWriter(file);
    printWriter.write(jsonMockSample());
    printWriter.flush();
    printWriter.close();

    Mock mock = jolyglot.fromJson(file, Mock.class);
    assertThat(jolyglot.toJson(mock), is(jsonMockSample()));
  }

  @Test public void fromFileJsonType() throws Exception {
    File file = temporaryFolder.newFile("test.txt");
    FileWriter printWriter = new FileWriter(file);
    printWriter.write(jsonMockParameterizedSample());
    printWriter.flush();
    printWriter.close();

    Method method = Types.class.getDeclaredMethod("mockParameterized");
    Type type = method.getGenericReturnType();

    MockParameterized<Mock> mockParameterized = jolyglot.fromJson(file, type);

    try {
      assertThat(jolyglot.toJson(mockParameterized, type),
          is(jsonMockParameterizedSample()));
    } catch (AssertionError i) {
      assertThat(jolyglot.toJson(mockParameterized, type),
          is(jsonMockParameterizedSampleReverse()));
    }
  }

  @Test public void fromFilePartialJsonType() throws Exception {
    File file = temporaryFolder.newFile("test.txt");
    FileWriter printWriter = new FileWriter(file);
    printWriter.write(jsonMockParameterizedSample());
    printWriter.flush();
    printWriter.close();

    Type type = jolyglot.newParameterizedType(MockParameterized.class, Object.class);
    MockParameterized mockParameterized = jolyglot.fromJson(file, type);

    try {
      assertThat(jolyglot.toJson(mockParameterized, type),
          is(jsonMockParameterizedSample()));
    } catch (AssertionError i) {
      assertThat(jolyglot.toJson(mockParameterized, type),
          is(jsonMockParameterizedSampleReverse()));
    }
  }

  @Test public void arrayOf() {
    Mock[] mocks = {new Mock(), new Mock()};
    Class classMocksArray = mocks.getClass();
    GenericArrayType genericArrayType = jolyglot.arrayOf(classMocksArray);

    Type type = genericArrayType.getGenericComponentType();
    mocks = jolyglot.fromJson(jsonMockArraySample(), type);
    assertNotNull(mocks[0]);
    assertNotNull(mocks[1]);
  }

  @Test public void parameterizedTypeWithOwner() {
    Type type = jolyglot.newParameterizedType(List.class, Mock.class);

    List<Mock> mocks = jolyglot.fromJson(jsonMockArraySample(), type);
    assertThat(jolyglot.toJson(mocks), is(jsonMockArraySample()));
  }

  private String jsonMockSample() {
    return "{\"s1\":\"s1\"}";
  }

  private String jsonMockArraySample() {
    return "[{\"s1\":\"s1\"},{\"s1\":\"s1\"}]";
  }

  private String jsonMockParameterizedSample() {
    return "{\"t\":{\"s1\":\"s1\"},\"s1\":\"s1\"}";
  }

  private String jsonMockParameterizedSampleReverse() {
    return "{\"s1\":\"s1\",\"t\":{\"s1\":\"s1\"}}";
  }

  private interface Types {
    Mock mock();
    MockParameterized<Mock> mockParameterized();
  }

  protected abstract Jolyglot jolyglot();
}
