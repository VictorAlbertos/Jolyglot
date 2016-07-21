package io.victoralbertos.jolyglot;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Base test to be extended from every json provider which supports generics.
 */
public abstract class JolyglotGenericsTest {
  private JolyglotGenerics jolyglot;
  @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Before public void setUp() {
    jolyglot = jolyglot();
  }

  @Test public void toJsonUsingTypeWithNotParameterizedType()
      throws NoSuchMethodException {
    Method method = Types.class.getDeclaredMethod("mock");
    Type type = method.getGenericReturnType();

    Mock mock = new Mock();
    String jsonMock = jolyglot.toJson(mock, type);
    assertThat(jsonMock, is(jsonMockSample()));
  }

  @Test public void fromStringJsonTypeWithNotParameterizedType() throws NoSuchMethodException {
    Method method = Types.class.getDeclaredMethod("mock");
    Type type = method.getGenericReturnType();

    Mock mock = jolyglot.fromJson(jsonMockParameterizedSample(), type);

    try {
      assertThat(jolyglot.toJson(mock, type),
          is(jsonMockSample()));
    } catch (AssertionError i) {
      assertThat(jolyglot.toJson(mock, type),
          is(jsonMockSample()));
    }
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

  //Type collections tests.

  @Test public void listToJson() throws NoSuchMethodException {
    Method method = Types.class.getDeclaredMethod("mockList");
    Type type = method.getGenericReturnType();
    List<Mock> mocks = new ArrayList<>();
    mocks.add(new Mock());

    String json = jolyglot.toJson(mocks, type);
    assertThat(json, is(jsonMockListSample()));
  }

  @Test public void jsonToList() throws NoSuchMethodException {
    Method method = Types.class.getDeclaredMethod("mockList");
    Type type = method.getGenericReturnType();

    List<Mock> mocks = jolyglot.fromJson(jsonMockListSample(), type);

    assertThat(jolyglot.toJson(mocks, type),
        is(jsonMockListSample()));
  }

  @Test public void parameterizedListToJson() throws NoSuchMethodException {
    Method method = Types.class.getDeclaredMethod("mockParameterizedList");
    Type type = method.getGenericReturnType();

    List<Mock> mocks = new ArrayList<>();
    mocks.add(new Mock());
    MockParameterized<List<Mock>> listMockParameterized = new MockParameterized<>(mocks);

    String json = jolyglot.toJson(listMockParameterized, type);

    try {
      assertThat(json, is(jsonMockParameterizedListSample()));
    } catch (AssertionError i) {
      assertThat(json, is(jsonMockParameterizedListSampleReverse()));
    }
  }

  @Test public void jsonToParameterizedList() throws NoSuchMethodException {
    Method method = Types.class.getDeclaredMethod("mockParameterizedList");
    Type type = method.getGenericReturnType();

    MockParameterized<List<Mock>> listMockParameterized =
        jolyglot.fromJson(jsonMockParameterizedListSample(), type);

    try {
      assertThat(jolyglot.toJson(listMockParameterized, type),
          is(jsonMockParameterizedListSample()));
    } catch (AssertionError i) {
      assertThat(jolyglot.toJson(listMockParameterized, type),
          is(jsonMockParameterizedListSampleReverse()));
    }
  }

  @Test public void mapToJson() throws NoSuchMethodException {
    Method method = Types.class.getDeclaredMethod("mockMap");
    Type type = method.getGenericReturnType();
    Map<Integer, Mock> mocks = new HashMap<>();
    mocks.put(1, new Mock());

    String json = jolyglot.toJson(mocks, type);
    assertThat(json, is(jsonMockMapSample()));
  }

  @Test public void jsonToMap() throws NoSuchMethodException {
    Method method = Types.class.getDeclaredMethod("mockMap");
    Type type = method.getGenericReturnType();

    Map<Integer, Mock> mocks = jolyglot.fromJson(jsonMockMapSample(), type);

    assertThat(jolyglot.toJson(mocks, type),
        is(jsonMockMapSample()));
  }

  @Test public void parameterizedMapToJson() throws NoSuchMethodException {
    Method method = Types.class.getDeclaredMethod("mockParameterizedMap");
    Type type = method.getGenericReturnType();

    Map<Integer, Mock> mocks = new HashMap<>();
    mocks.put(1, new Mock());
    MockParameterized<Map<Integer, Mock>> mapMockParameterized = new MockParameterized<>(mocks);

    String json = jolyglot.toJson(mapMockParameterized, type);

    try {
      assertThat(json,
          is(jsonMockParameterizedMapSample()));
    } catch (AssertionError i) {
      assertThat(json,
          is(jsonMockParameterizedMapSampleReverse()));
    }
  }

  @Test public void jsonToParameterizedMap() throws NoSuchMethodException {
    Method method = Types.class.getDeclaredMethod("mockParameterizedMap");
    Type type = method.getGenericReturnType();

    MockParameterized<Map<Integer, Mock>> mapMockParameterized =
        jolyglot.fromJson(jsonMockParameterizedMapSample(), type);

    try {
      assertThat(jolyglot.toJson(mapMockParameterized, type),
          is(jsonMockParameterizedMapSample()));
    } catch (AssertionError i) {
      assertThat(jolyglot.toJson(mapMockParameterized, type),
          is(jsonMockParameterizedMapSampleReverse()));
    }
  }

  private String jsonMockSample() {
    return "{\"s1\":\"s1\"}";
  }

  private String jsonMockParameterizedSample() {
    return "{\"t\":{\"s1\":\"s1\"},\"s1\":\"s1\"}";
  }

  private String jsonMockParameterizedSampleReverse() {
    return "{\"s1\":\"s1\",\"t\":{\"s1\":\"s1\"}}";
  }

  private String jsonMockArraySample() {
    return "[{\"s1\":\"s1\"},{\"s1\":\"s1\"}]";
  }

  private String jsonMockListSample() {
    return "[{\"s1\":\"s1\"}]";
  }

  private String jsonMockParameterizedListSample() {
    return "{\"t\":[{\"s1\":\"s1\"}],\"s1\":\"s1\"}";
  }

  private String jsonMockParameterizedListSampleReverse() {
    return "{\"s1\":\"s1\",\"t\":[{\"s1\":\"s1\"}]}";
  }

  private String jsonMockMapSample() {
    return "{\"1\":{\"s1\":\"s1\"}}";
  }

  private String jsonMockParameterizedMapSample() {
    return "{\"t\":{\"1\":{\"s1\":\"s1\"}},\"s1\":\"s1\"}";
  }

  private String jsonMockParameterizedMapSampleReverse() {
    return "{\"s1\":\"s1\",\"t\":{\"1\":{\"s1\":\"s1\"}}}";
  }

  private interface Types {
    Mock mock();
    MockParameterized<Mock> mockParameterized();

    List<Mock> mockList();
    MockParameterized<List<Mock>> mockParameterizedList();

    Map<Integer, Mock> mockMap();
    MockParameterized<Map<Integer, Mock>> mockParameterizedMap();
  }

  protected abstract JolyglotGenerics jolyglot();

}
