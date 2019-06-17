gradle clean build
gradle collectJars
tar -czvf alertmanager.tar build/libs/*