**<p align="center"> Good programming practices </p>**
_________________________________
**<p align="center"> Wrocław University of Science and Technology </p>**
**<p align="center"> Computer Science, Faculty of Electronics, 6 semester </p>**
**<p align="center"> Radosław Lis, 241385 </p>**

# Table of Contents
- [General info](#desc)
- [Prerequisites](#pre)
- [Configurations](#conf)
  *  [JavaFX](#jav)
  *  [Scene Builder](#scene)
  *  [UTF-8 Encoding](#utf)
  *  [Common errors](#err)

<a name="desc"></a>

# General info
Program made for university course *Good programming practices*.
<a name="tech1"></a>
### Technologies 

### Running
Checkout on *aplikacj* branch and run *Main.java*
### Description
<img src="https://i.imgur.com/zkRc4Yy.png" width="555" height="456" />

<a name="pre"></a>
# Prerequisites
- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) 
- [Eclipse](https://www.eclipse.org/downloads/)

# Configurations

<a name="jav"></a>
## JavaFX
1. Go **Window** -> **Preferences** -> **Java** -> **Build Path** -> **User Libraries** -> **New** and write JAVAFX\_11. Then left click on JAVAFX_11, click Add External JARs... 
and add all JARs from *...\javafx-sdk-11.0.2\lib*.
2. Right click on the project and go **Build Path** -> **Configure Build Path** -> **Add Library** -> **User Library** -> **Next** and choose JAVAFX_11.
3. Go **Run** -> **Run Configurations** -> **Arguments** and in VM arguments field write:
```
--module-path "xyz\javafx-sdk-11.0.2\lib" --add-modules=javafx.controls,javafx.fxml
```
(**xyz** to your path to javafx-sdk). Then **Apply** -> **Close**.

___________________________________
<a name="scene"></a>
## Scene Builder
1. Go to [**Gluon** site](https://gluonhq.com/products/scene-builder/#download) and in section **Download Scene Builder for Java 11** download version for your OS.
2. Go **Window** -> **Preferences** -> **JavaFX** and in *SceneBuilder executable* field click browse and find your .exe file (*...\SceneBuilder\SceneBuilder.exe*) and in *JavaFX 11+ SDK* field click browse and find your sdk folder (*...\javafx-sdk-11.0.2*)
___________________________________
<a name="utf"></a>
## UTF-8 Encoding
1. Go **Window** -> **Preferences** -> **General** -> **Workspace** -> and in *Text file encoding* field choose **Other** -> **UTF-8**.

___________________________________
<a name="err"></a>
## Common errors
If any of this:
```
Exception in thread "WindowsNativeRunloopThread" java.lang.NoSuchMethodError: 
Exception in thread "JavaFX Application Thread" java.lang.NullPointerException
```

In the enviroment variable JAVA_HOME set the folder on Java 11 JDK (*C:\Program Files\Java\jdk-11.0.6*)
