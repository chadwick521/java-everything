# java-everything

##使用代码

gradle项目请引入：

```
repositories {

    //maven私服
    maven {
        url 'http://47.93.59.217:8081/repository/maven-public/'
    }

    // maven中央仓库
    mavenCentral()
}
```

maven项目请引入仓库：

```xml
<repositories>
  <repository>
    <id>public</id>
    <name>public Repository</name>
    <url>http://47.93.59.217:8081/repository/maven-public/</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>

<pluginRepositories>
  <pluginRepository>
    <id>public</id>
    <name>Public Repositories</name>
    <url>http://47.93.59.217:8081/repository/maven-public/</url>
  </pluginRepository>
</pluginRepositories>
```