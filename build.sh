gradle clean build -x test
gradle collectJars
tar -czvf slack.tar build/libs/*