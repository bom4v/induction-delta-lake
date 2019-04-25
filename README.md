Delta Lake Tutorial
===================

# References
* [Delta Lake](https://delta.io)
* [How to install Python virtual environments with Pyenv and `pipenv`](http://github.com/machine-learning-helpers/induction-python/tree/master/installation/virtual-env)

# Overview
That repository aims to provide a simple working environment
reproducing the steps given in the
[Delta Lake quickstart guide](https://docs.delta.io/latest/delta-intro.html#quickstart).

# Installation
A convenient way to get the Spark ecosystem and CLI (command-line interface)
tools (_e.g._, `spark-submit`, `spark-shell`, `spark-sql`, `beeline`,
`pyspark` and `sparkR`) is through
[PySpark](https://spark.apache.org/docs/latest/api/python/pyspark.html).
PySpark is a Python wrapper around Spark libraries, run through
a Java Virtual Machine (JVM) handily provided by
[OpenJDK](https://openjdk.java.net/).

To guarantee a full reproducibility with the Python stack, `pyenv`
and `pipenv` are used here.
Also, `.python_version` and `Pipfile` are part of the project.
The user has therefore just to install `pyenv` and `pipenv`,
and then all the commands described in this document become easily
accessible and reproducible.

Follow the instructions on
[how-to install Python and Java for Spark](http://github.com/machine-learning-helpers/induction-python/tree/master/installation/virtual-env)
for more details. Basically:
* The `pyenv` utility is installed from
  [GitHub](https://github.com/pyenv/pyenv.git)
* Specific versions of Python (namely, at the time of writing, 2.7.15
  and 3.7.2) are installed in the user account file-system.
  Those specific Python frameworks provide the `pip` package management tool
* The Python `pipenv` package is installed thanks to `pip`
* The `.python_version`, `Pipfile` and `Pipfile.lock` files, specific
  per project folder, fully drive the versions of all the Python packages
  and of Python itself, so as to guarantee full reproducibility
  on all the platforms

## Clone the Git repository
```bash
$ mkdir -p ~/dev/infra && cd ~/dev/infra
$ git clone https://github.com/bom4v/induction-delta-lake.git
$ cd ~/dev/infra/induction-delta-lake
```

## Java
* Once an OpenJDK JVM has been installed, specify `JAVA_HOME` accordingly
  in `~/.bashrc`
  
* Maven also needs to be installed

### Debian/Ubuntu
```bash
$ sudo aptitude -y install openjdk-8-jdk maven
$ export JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64"
```

### Fedora/CentOS/RedHat
```bash
$ sudo dnf -y install java-1.8.0-openjdk maven
$ export JAVA_HOME="/usr/lib/jvm/java-1.8.0-openjdk"
```

### MacOS
* Visit https://jdk.java.net/8/, download and install the MacOS package
```bash
$ export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
$ brew install maven
```

## SBT
[Download and install SBT](https://www.scala-sbt.org/download.html)

## Python-related dependencies
* `pyenv`:
```bash
$ git clone https://github.com/pyenv/pyenv.git ${HOME}/.pyenv
$ cat >> ~/.bashrc << _EOF

# Pyenv
# git clone https://github.com/pyenv/pyenv.git \${HOME}/.pyenv
export PATH=\${PATH}:\${HOME}/.pyenv/bin
if command -v pyenv 1>/dev/null 2>&1
then
  eval "\$(pyenv init -)"
fi

_EOF
$ . ~/.bashrc
$ pyenv install 2.7.15 && pyenv install 3.7.2
```

* `pip` and `pipenv`:
```bash
$ cd ~/dev/infra/spark-submit-sql
$ pyenv versions
  system
* 2.7.15 (set by ~/dev/infra/spark-submit-sql/.python-version)
  3.7.2
$ python --version
Python 2.7.15
$ pip install -U pip pipenv
$ pipenv install
Creating a virtualenv for this project...
Pipfile: ~/dev/infra/spark-submit-sql/Pipfile
Using ~/.pyenv/versions/2.7.15/bin/python (2.7.15) to create virtualenv...
⠇ Creating virtual environment...Using base prefix '~/.pyenv/versions/2.7.15'
New python executable in ~/.local/share/virtualenvs/spark-submit-sql-nFz46YtK/bin/python
Installing setuptools, pip, wheel...
done.
Running virtualenv with interpreter ~/.pyenv/versions/2.7.15/bin/python
✔ Successfully created virtual environment! 
Virtualenv location: ~/.local/share/virtualenvs/spark-submit-sql-nFz46YtK
Installing dependencies from Pipfile.lock (d2363d)...
  �   ▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉▉ 2/2 — 00:00:20
To activate this project's virtualenv, run pipenv shell.
Alternatively, run a command inside the virtualenv with pipenv run.
```

# Use the command-line script
* Download the application JAR artefact from a
  [Maven repository](https://repo1.maven.org/maven2/):
```bash
$ wget https://oss.sonatype.org/content/groups/public/org/bom4v/ti/delta-tutorial_2.12/0.0.1-spark2.4/delta-tutorial_2.12-0.0.1-spark2.4.jar
```

* Launch the JAR application:
```bash
$ spark-submit --packages io.delta:delta-core_2.12:0.1.0 --master yarn --deploy-mode client --class org.bom4v.ti.DeltaLakeTutorial delta-tutorial_2.12-0.0.1-spark2.4.jar
```

# Contribute to that project

## Simple test
* Compile and package the SQL-based data extractor:
```bash
$ sbt 'set isSnapshot := true' compile package publishM2 publishLocal
[info] Loading settings for project global-plugins from gpg.sbt ...
[info] Loading global plugins from ~/.sbt/1.0/plugins
[info] Loading settings for project induction-delta-lake-build from plugins.sbt ...
[info] Loading project definition from ~/dev/infra/induction-delta-lake/project
[info] Loading settings for project induction-delta-lake from build.sbt ...
[info] Set current project to delta-tutorial (in build file:~/dev/infra/induction-delta-lake/)
[info] Executing in batch mode. For better performance use sbt's shell
[info] Defining isSnapshot
[info] The new value will be used by makeIvyXmlConfiguration, makeIvyXmlLocalConfiguration and 5 others.
[info] 	Run `last` for details.
[info] Reapplying settings...
[info] Set current project to delta-tutorial (in build file:~/dev/infra/induction-delta-lake/)
[success] Total time: 1 s, completed Apr 25, 2019 6:59:10 PM
[success] Total time: 0 s, completed Apr 25, 2019 6:59:10 PM
[info] Packaging ~/dev/infra/induction-delta-lake/target/scala-2.12/delta-tutorial_2.12-0.0.1-spark2.4-sources.jar ...
[info] Done packaging.
[info] Wrote ~/dev/infra/induction-delta-lake/target/scala-2.12/delta-tutorial_2.12-0.0.1-spark2.4.pom
[info] Main Scala API documentation to ~/dev/infra/induction-delta-lake/target/scala-2.12/api...
model contains 5 documentable templates
[info] Main Scala API documentation successful.
[info] Packaging ~/dev/infra/induction-delta-lake/target/scala-2.12/delta-tutorial_2.12-0.0.1-spark2.4-javadoc.jar ...
[info] Done packaging.
[info] 	published delta-tutorial_2.12 to file:~/.m2/repository/org/bom4v/ti/delta-tutorial_2.12/0.0.1-spark2.4/delta-tutorial_2.12-0.0.1-spark2.4.pom
[info] 	published delta-tutorial_2.12 to file:~/.m2/repository/org/bom4v/ti/delta-tutorial_2.12/0.0.1-spark2.4/delta-tutorial_2.12-0.0.1-spark2.4.jar
[info] 	published delta-tutorial_2.12 to file:~/.m2/repository/org/bom4v/ti/delta-tutorial_2.12/0.0.1-spark2.4/delta-tutorial_2.12-0.0.1-spark2.4-sources.jar
[info] 	published delta-tutorial_2.12 to file:~/.m2/repository/org/bom4v/ti/delta-tutorial_2.12/0.0.1-spark2.4/delta-tutorial_2.12-0.0.1-spark2.4-javadoc.jar
[success] Total time: 3 s, completed Apr 25, 2019 6:59:13 PM
[info] Wrote ~/dev/infra/induction-delta-lake/target/scala-2.12/delta-tutorial_2.12-0.0.1-spark2.4.pom
[info] Main Scala API documentation to ~/dev/infra/induction-delta-lake/target/scala-2.12/api...
model contains 5 documentable templates
[info] Main Scala API documentation successful.
[info] Packaging ~/dev/infra/induction-delta-lake/target/scala-2.12/delta-tutorial_2.12-0.0.1-spark2.4-javadoc.jar ...
[info] Done packaging.
[info] :: delivering :: org.bom4v.ti#delta-tutorial_2.12;0.0.1-spark2.4 :: 0.0.1-spark2.4 :: integration :: Thu Apr 25 18:59:15 EEST 2019
[info] 	delivering ivy file to ~/dev/infra/induction-delta-lake/target/scala-2.12/ivy-0.0.1-spark2.4.xml
[info] 	published delta-tutorial_2.12 to ~/.ivy2/local/org.bom4v.ti/delta-tutorial_2.12/0.0.1-spark2.4/poms/delta-tutorial_2.12.pom
[info] 	published delta-tutorial_2.12 to ~/.ivy2/local/org.bom4v.ti/delta-tutorial_2.12/0.0.1-spark2.4/jars/delta-tutorial_2.12.jar
[info] 	published delta-tutorial_2.12 to ~/.ivy2/local/org.bom4v.ti/delta-tutorial_2.12/0.0.1-spark2.4/srcs/delta-tutorial_2.12-sources.jar
[info] 	published delta-tutorial_2.12 to ~/.ivy2/local/org.bom4v.ti/delta-tutorial_2.12/0.0.1-spark2.4/docs/delta-tutorial_2.12-javadoc.jar
[info] 	published ivy to ~/.ivy2/local/org.bom4v.ti/delta-tutorial_2.12/0.0.1-spark2.4/ivys/ivy.xml
[success] Total time: 1 s, completed Apr 25, 2019 6:59:15 PM
```

* The above command generates JAR artefacts (mainly
  `delta-tutorial_2.12-0.0.1-spark2.4.jar`) locally in the project
  `target` directory, as well as in the Maven and Ivy2 user repositories
  (`~/.m2` and `~/.ivy2` respectively).

* The `set isSnapshot := true` option allows to silently override
  any previous versions of the JAR artefacts in the Maven and Ivy2 repositories
  
* Check that the artefacts have been produced
  + Locally (`package` command):
```bash
$ ls -laFh target/scala-2.12/delta-tutorial_2.12-0.0.1-spark2.4.jar
-rw-r--r-- 1 user group 5.4K Apr 25 12:16 target/scala-2.12/delta-tutorial_2.12-0.0.1-spark2.4.jar
```

  + In the local Maven repository (`publishM2` task):
```bash
$ ls -laFh ~/.m2/repository/org/bom4v/ti/delta-tutorial_2.12/0.0.1-spark2.4/delta-tutorial_2.12-0.0.1-spark2.4.jar
-rw-r--r-- 1 user group 5.4K Apr 25 12:16 ~/.m2/repository/org/bom4v/ti/delta-tutorial_2.12/0.0.1-spark2.4/delta-tutorial_2.12-0.0.1-spark2.4.jar
```

  + In the local Ivy2 repository (`publishLocal` task):
```bash
$ ls -laFh ~/.ivy2/local/org.bom4v.ti/delta-tutorial_2.12/0.0.1-spark2.4/jars/delta-tutorial_2.12.jar
-rw-r--r-- 1 user group 5.4K Apr 25 12:16 ~/.ivy2/local/org.bom4v.ti/delta-tutorial_2.12/0.0.1-spark2.4/jars/delta-tutorial_2.12.jar
```

* Clean any previous data:
```bash
$ rm -rf /tmp/delta-table
```

* Launch the job in the SBT JVM:
```bash
$ sbt "runMain org.bom4v.ti.DeltaLakeTutorial"
[info] Loading settings for project global-plugins from gpg.sbt ...
[info] Loading global plugins from ~/.sbt/1.0/plugins
[info] Loading settings for project induction-delta-lake-build from plugins.sbt ...
[info] Loading project definition from ~/dev/infra/induction-delta-lake/project
[info] Loading settings for project induction-delta-lake from build.sbt ...
[info] Set current project to delta-tutorial (in build file:~/dev/infra/induction-delta-lake/)
[info] Running org.bom4v.ti.DeltaLakeTutorial 
Spark: 2.4.2  -  Scala: version 2.12.8
19/04/25 18:50:35 INFO org.apache.parquet.hadoop.codec.CodecConfig: Compression: SNAPPY
...
Latest version of the data:
+---+
| id|
+---+
|  7|
|  8|
|  5|
|  9|
|  6|
+---+

Older version of the data:
+---+
| id|
+---+
|  1|
|  0|
|  3|
|  2|
|  4|
+---+

[success] Total time: 29 s, completed Apr 25, 2019 6:50:51 PM
```

* It generates parquet files in the `/tmp/delta-table`:
```bash
$ ls -laFh /tmp/delta-table
total 192
drwxr-xr-x  27 user  group   864B Apr 25 19:06 ./
drwxrwxrwt  13 root     group   416B Apr 25 19:07 ../
-rw-r--r--   1 user  group    12B Apr 25 19:06 .part-00000-7e074f5c-b50b-417a-b001-b137eacd13ec-c000.snappy.parquet.crc
[...]
e-07bdab889fdc-c000.snappy.parquet.crc
-rw-r--r--   1 user  group    12B Apr 25 19:06 .part-00011-e9ffc871-42d5-402b-bf26-1bcd63bc2e9e-c000.snappy.parquet.crc
drwxr-xr-x   6 user  group   192B Apr 25 19:06 _delta_log/
-rw-r--r--   1 user  group   263B Apr 25 19:06 part-00000-7e074f5c-b50b-417a-b001-b137eacd13ec-c000.snappy.parquet
[...]
-rw-r--r--   1 user  group   423B Apr 25 19:06 part-00011-e9ffc871-42d5-402b-bf26-1bcd63bc2e9e-c000.snappy.parquet
```

* Launch the job with `spark-submit`
  + In local mode (for instance, on a laptop; that mode may not always work
    on the Spark/Hadoop clusters):
```bash
$ pipenv run spark-submit --packages io.delta:delta-core_2.12:0.1.0 --master local --class org.bom4v.ti.DeltaLakeTutorial target/scala-2.12/delta-tutorial_2.12-0.0.1-spark2.4.jar
2019-02-18 20:22:46 INFO  SparkContext:54 - Running Spark version 2.4.2
2019-02-18 20:22:46 INFO  SparkContext:54 - Submitted application: StandaloneQuerylauncher
...
Spark: 2.4.2
Scala: version 2.12.8
...
2019-02-18 20:22:47 INFO  SparkContext:54 - Successfully stopped SparkContext
2019-02-18 20:22:47 INFO  ShutdownHookManager:54 - Shutdown hook called
...
```

  + In Yarn cluster client mode with the standalone version (that method
    is basically the same as above):
```bash
$ pipenv run spark-submit --packages io.delta:delta-core_2.12:0.1.0 --num-executors 1 --executor-memory 512m --master yarn --deploy-mode client --class org.bom4v.ti.DeltaLakeTutorial target/scala-2.12/delta-tutorial_2.12-0.0.1-spark2.4.jar
...
Spark: 2.4.2
Scala: version 2.12.8
...
```

* Playing with Python on PySpark:
```bash
$ rm -rf /tmp/delta-table
$ pipenv run pyspark --packages io.delta:delta-core_2.12:0.1.0
Python 2.7.16 (default, Mar  4 2019, 09:01:38) 
[GCC 4.2.1 Compatible Apple LLVM 10.0.0 (clang-1000.11.45.5)] on darwin
Type "help", "copyright", "credits" or "license" for more information.
2019-04-25 19:11:59 WARN  NativeCodeLoader:62 - Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
Setting default log level to "WARN".
To adjust logging level use sc.setLogLevel(newLevel). For SparkR, use setLogLevel(newLevel).
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /__ / .__/\_,_/_/ /_/\_\   version 2.4.0
      /_/

Using Python version 2.7.16 (default, Mar  4 2019 09:01:38)
SparkSession available as 'spark'.
>>> data = spark.range(0, 5)
>>> data.write.format("delta").save("/tmp/delta-table")
>>> data = spark.range(5, 10)
>>> data.write.format("delta").mode("overwrite").save("/tmp/delta-table")
>>> df = spark.read.format("delta").load("/tmp/delta-table")
>>> df.show()
+---+
| id|
+---+
|  7|
|  8|
|  5|
|  9|
|  6|
+---+
>>> df = spark.read.format("delta").option("versionAsOf", 0).load("/tmp/delta-table")
>>> df.show()
+---+
| id|
+---+
|  1|
|  0|
|  3|
|  2|
|  4|
+---+
>>> quit()
```
