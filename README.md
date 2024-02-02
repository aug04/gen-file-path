# How to build?
- Navigate to pom.xml's folder
- Run this command:

  `mvn clean package -DskipTests`

- After build success, can find the jar in folder `target`

# How to run?
- Require Java 11

- Parameters (all parameters are optional):
  - `--target` => folder path to scan, leave empty to scan the current folder
  - `-f` or `--filename` => output file name, default file name is `FilesChanged.txt`
  - `-d` or `--dir` => Destination folder to store the output file
  - `-t` or `--type` => accept 2 values: `windows` `linux` (lowercase), if `windows` the path separator will be `\`, else will be `/`, default separator depends on current OS
  - `--absolutePath` => Log absolute path, default is relative path
  
- Sample:
  
  `java -jar gen-file-path.jar --target /usr/share/code -d /usr/share/log -f FileList.txt -t linux --absolutePath`
