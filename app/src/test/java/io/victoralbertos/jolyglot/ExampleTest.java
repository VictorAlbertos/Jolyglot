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

import java.lang.reflect.Type;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(Theories.class)
public class ExampleTest {
  @DataPoint public static JolyglotGenerics gson = new GsonSpeaker();
  @DataPoint public static JolyglotGenerics jackson = new JacksonSpeaker();
  @DataPoint public static JolyglotGenerics moshi = new MoshiSpeaker();

  @Theory
  @Test public void objectToJson(Jolyglot jolyglot) {
    Mock mock = new Mock("s1");
    String json = jolyglot.toJson(mock);
    assertThat(json, is("{\"s1\":\"s1\"}"));
  }

  @Theory
  @Test public void genericToJson(JolyglotGenerics jolyglot) {
    Mock mock = new Mock("s1");
    Wrapper<Mock> wrapper = new Wrapper<>(mock);

    Type type = jolyglot.newParameterizedType(Wrapper.class, Mock.class);
    String json = jolyglot.toJson(wrapper, type);
    assertThat(json, is("{\"t\":{\"s1\":\"s1\"}}"));
  }

  @Theory
  @Test public void jsonToObject(Jolyglot jolyglot) {
    String json = "{\"s1\":\"s1\"}";
    Mock mock = jolyglot.fromJson(json, Mock.class);
    assertNotNull(mock);
  }

  @Theory
  @Test public void jsonToGeneric(JolyglotGenerics jolyglot) {
    String json = "{\"t\":{\"s1\":\"s1\"}}";

    Type type = jolyglot.newParameterizedType(Wrapper.class, Mock.class);
    Wrapper<Mock> wrapper = jolyglot.fromJson(json, type);
    assertNotNull(wrapper);
    assertNotNull(wrapper.getT());
  }

}
