mkdir -p bin
javac -cp "lib/*" -d bin $(find src -name "*.java")
