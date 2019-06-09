gradle clean build
gradle collectJars
tar -czvf slack.tar build/libs/*