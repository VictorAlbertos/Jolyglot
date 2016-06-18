## Jolyglot
Jolyglot allows to convert objects to and from Json without depending on any concrete implementation. Thus, you can happy code agains this [polyglot abstraction](https://github.com/VictorAlbertos/Jolyglot/blob/master/api/src/main/java/io/victoralbertos/jolyglot/Jolyglot.java), and let the clients of your library choose whatever json provider which better suits their needs. 

## Available Json providers:
* [Gson](https://github.com/google/gson). 
* [Jackson](https://github.com/FasterXML/jackson). 
* [Moshi](https://github.com/square/moshi). 

If you need another json provider, feel free to open an issue to address it. 

## Setup
Add JitPack repository in your build.gradle (top level module):
```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

And add Jolyglot api module in the build.gradle of your library module:
```gradle
dependencies {
    compile "com.github.VictorAlbertos.Jolyglot:api:0.0.1"
}
```

Ask to the clients of your librery to add one of the next json providers:

```gradle
dependencies {
    compile "com.github.VictorAlbertos.Jolyglot:gson:0.0.1"
    compile "com.github.VictorAlbertos.Jolyglot:jackson:0.0.1"
    compile "com.github.VictorAlbertos.Jolyglot:moshi:0.0.1"
}
```

## Usage

### Instantiate Jolyglot.
Ask to the client of your library for a concrete implementation of [Jolyglot](https://github.com/VictorAlbertos/Jolyglot/blob/master/api/src/main/java/io/victoralbertos/jolyglot/Jolyglot.java).

Depending on the provider chosen by your client, the instance of Jolyglot will be created in one of the nex ways:

```java
Jolyglot jolyglot = new GsonSpeaker()
Jolyglot jolyglot = new GsonSpeaker(gson) //overloaded constructor to customize the gson object.

Jolyglot jolyglot = new JacksonSpeaker()
Jolyglot jolyglot = new JacksonSpeaker(objectMapper) //overloaded constructor to customize the objectMapper object.

Jolyglot jolyglot = new MoshiSpeaker()
Jolyglot jolyglot = new MoshiSpeaker(moshi) //overloaded constructor to customize the moshi object.
```

#### Object to json.
```java
jolyglot.toJson(object);
```

#### Parameterized object to json.
```java
Type type = jolyglot.newParameterizedType(YourParameterized.class, YourEnclosing.class);
jolyglot.toJson(parameterizedObject, type);
```

#### Json to object.
```java
String json = "";
jolyglot.fromJson(json, Your.class);
```

#### Json to parameterized object.
```java
String json = "";
Type type = jolyglot.newParameterizedType(YourParameterized.class, YourEnclosing.class);
jolyglot.fromJson(json, type);
```

For a complete example go [here](https://github.com/VictorAlbertos/Jolyglot/blob/master/app/src/test/java/io/victoralbertos/jolyglot/ExampleTest.java). 

Be aware that the idiosyncrasy of every underlying json provider still remains. Jolyglot is only an abstraction layer to honor this diversity, but, in the end, every java class serialized/deserialized neeeds to fulfill the requirements of every json provider.
