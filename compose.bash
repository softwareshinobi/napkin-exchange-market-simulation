rm -rf target

mvn install -DskipTests

java -jar target/napkin-exchange-server-1.0-SNAPSHOT.jar
