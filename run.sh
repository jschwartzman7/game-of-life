mkdir -p bin
javac -cp "lib/*" -d bin $(find src -name "*.java")
echo "Program Compiled Successfully"
java -cp "lib/*:bin" GameOfLife
echo "Program Ended"
