# Compile Code (Use Preview)

>javac -d bin src\server\\\*.java src\client\\\*.java -cp lib/mysql-connector-j-9.2.0.jar src\utils\\*.java

# Run Code 

 Server Side

* java -cp "bin;lib/mysql-connector-j-9.2.0.jar;." server.ChatServer

 Client Side

* java -cp bin/ client.ChatClient
