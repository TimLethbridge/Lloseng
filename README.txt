Student name: Wenglei Wu
Student number: 300136767
Email address: wwu077@uottawa.ca

Testcase 2001

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ServerConsole               
Server listening for connections on port 5555


Testcase 2002

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole        
Error: Login ID is not provided. Quit client. 
wuwenglei@wuwengleideMacBook-Pro simplechat1 % 



Testcase 2003

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole abcde
Error: Can't setup connection! Terminating client.
wuwenglei@wuwengleideMacBook-Pro simplechat1 % 


Testcase 2004

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ServerConsole
Server listening for connections on port 5555
Client abcde connects. 
New login from localhost (127.0.0.1), loginID: abcde

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole abcde
Connected!


Testcase 2005

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ServerConsole
Server listening for connections on port 5555
Client abcde connects. 
New login from localhost (127.0.0.1), loginID: abcde
Message received: Hello world! from localhost (127.0.0.1), loginID: abcde

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole abcde
Connected!
Hello world!
> abcde: Hello world!


Testcase 2006

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ServerConsole
Server listening for connections on port 5555
Client Tom connects. 
New login from localhost (127.0.0.1), loginID: Tom
Client Alice connects. 
New login from localhost (127.0.0.1), loginID: Alice
Client Jim connects. 
New login from localhost (127.0.0.1), loginID: Jim
Message received: I am Tom. from localhost (127.0.0.1), loginID: Tom
Message received: I am Alice. from localhost (127.0.0.1), loginID: Alice
Message received: I am Jim. from localhost (127.0.0.1), loginID: Jim
This is the server receiving message from Tom, Alice, and Jim.
SERVER MSG> This is the server receiving message from Tom, Alice, and Jim.

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Tom  
Connected!
I am Tom.
> Tom: I am Tom.
> Alice: I am Alice.
> Jim: I am Jim.
> SERVER MSG> This is the server receiving message from Tom, Alice, and Jim.

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Alice
Connected!
> Tom: I am Tom.
I am Alice.
> Alice: I am Alice.
> Jim: I am Jim.
> SERVER MSG> This is the server receiving message from Tom, Alice, and Jim.

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Jim
Connected!
> Tom: I am Tom.
> Alice: I am Alice.
I am Jim.
> Jim: I am Jim.
> SERVER MSG> This is the server receiving message from Tom, Alice, and Jim.


Testcase 2007

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ServerConsole
Server listening for connections on port 5555
#quit
Server has closed. 
wuwenglei@wuwengleideMacBook-Pro simplechat1 % 


Testcase 2008

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ServerConsole
Server listening for connections on port 5555
Client Sun connects. 
New login from localhost (127.0.0.1), loginID: Sun
#stop
Server has stopped listening for connections.
Server has stopped listening for connections.
Message received: Hello, I'm Sun. from localhost (127.0.0.1), loginID: Sun
#start
Server listening for connections on port 5555
Client Alice connects. 
New login from localhost (127.0.0.1), loginID: Alice
Client Jim connects. 
New login from localhost (127.0.0.1), loginID: Jim

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Sun
Connected!
> [Warning] Server has stopped listening for connections. 
Hello, I'm Sun.
> [Warning] Server has stopped listening for connections. 
> Sun: Hello, I'm Sun.

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Alice
Connected!

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Jim
Connected!


Testcase 2009

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ServerConsole
Server listening for connections on port 5555
Client Sun connects. 
New login from localhost (127.0.0.1), loginID: Sun
#stop
Server has stopped listening for connections.
Server has stopped listening for connections.
#close
Client Sun disconnects. 
Server has closed. 

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Sun
Connected!
> [Warning] Server has stopped listening for connections. 
SERVER SHUTTING DOWN! DISCONNECTING!
Abnormal termination of connection.
Connection closed! 
wuwenglei@wuwengleideMacBook-Pro simplechat1 % 


Testcase 2009

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ServerConsole
Server listening for connections on port 5555
Client Sun connects. 
New login from localhost (127.0.0.1), loginID: Sun
#close
Client Sun disconnects. 
Server has stopped listening for connections.
Server has closed. 
#start
Server listening for connections on port 5555
Client Sun connects. 
New login from localhost (127.0.0.1), loginID: Sun
Message received: This is a connect after a close and a start of the server. from localhost (127.0.0.1), loginID: Sun

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Sun
Connected!
SERVER SHUTTING DOWN! DISCONNECTING!
Abnormal termination of connection.
Connection closed! 
wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Sun
Connected!
This is a connect after a close and a start of the server.
> Sun: This is a connect after a close and a start of the server.


Testcase 2010

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ServerConsole
Server listening for connections on port 5555
Client Sun connects. 
New login from localhost (127.0.0.1), loginID: Sun
Client Sun disconnected. 

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Sun
Connected!
#quit
Connection closed! 
wuwenglei@wuwengleideMacBook-Pro simplechat1 % 


Testcase 2011

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ServerConsole
Server listening for connections on port 5555
Client Sun connects. 
New login from localhost (127.0.0.1), loginID: Sun
Client Sun disconnected. 

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Sun
Connected!
#logoff
Connection closed! 


Testcase 2012

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ServerConsole
Server listening for connections on port 5555
Client Sun connects. 
New login from localhost (127.0.0.1), loginID: Sun
Client Sun disconnected. 

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Sun
Connected!
#logoff
Connection closed! 
#sethost localhost2
Host set to: localhost2
#setport 1234
Port set to: 1234


Testcase 2013

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ServerConsole 1234
Server listening for connections on port 1234


Testcase 2017

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ServerConsole
Server listening for connections on port 5555
Client Sun connects. 
New login from localhost (127.0.0.1), loginID: Sun
Client Alice connects. 
New login from localhost (127.0.0.1), loginID: Alice
Client Jim connects. 
New login from localhost (127.0.0.1), loginID: Jim
Client Sun disconnected. 
Client Alice disconnected. 

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Sun
Connected!
> Alice has connected.
> Jim has connected.
#quit
Connection closed! 
wuwenglei@wuwengleideMacBook-Pro simplechat1 % 

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Alice
Connected!
> Jim has connected.
>  Sun has disconnected.
#logoff
Connection closed! 

wuwenglei@wuwengleideMacBook-Pro simplechat1 % java ClientConsole Jim
Connected!
>  Sun has disconnected.
>  Alice has disconnected.
