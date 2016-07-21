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

import com.alibaba.fastjson.JSON;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * FastJson implementation of Jolyglot
 */
public class FastJsonSpeaker implements Jolyglot {

  /**
   * {@inheritDoc}
   */
  @Override public String toJson(Object src) {
    return JSON.toJSONString(src);
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(String json, Class<T> classOfT) throws RuntimeException {
    return JSON.parseObject(json, classOfT);
  }

  /**
   * {@inheritDoc}
   */
  @Override public <T> T fromJson(File file, Class<T> classOfT) throws RuntimeException {
    InputStream fileInputStream = null;
    try {
      fileInputStream = new FileInputStream(file);
      return JSON.parseObject(fileInputStream, classOfT);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (fileInputStream != null)
        try {
          fileInputStream.close();
        } catch (IOException i) {}
    }
  }
}
