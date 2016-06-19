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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.core.Is.is;
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

  @Test public void fromStringJsonClass() {
    Mock mock = jolyglot.fromJson(jsonMockSample(), Mock.class);
    assertThat(jolyglot.toJson(mock), is(jsonMockSample()));
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

  private String jsonMockSample() {
    return "{\"s1\":\"s1\"}";
  }

  protected abstract Jolyglot jolyglot();
}
