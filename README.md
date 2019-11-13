# Mark, Mads J, Mads W & Jesper P

*Kotlin opgave lavet af: Anders Kalhauge & Tobias Grundtvig* 

> The task is to create a web server which will use any class that implements a
simple interface as content. The interface’s only jobs are to mark a class as
fit for web publishing and to provide a method, that tells the class to persist
its memory

**The content class should have functions corresponding to RESTful methods
and urls:**

*1: the url `/member` requested with the GET method corresponds to the
method `getMember():` `List<Member>`*

*2: the url `/member/<integer>` also with GET corresponds to `getMember(id:
Int):` `Member?`*
  
*3: the url `/member` requested with PUT and a JSON member in the body,
corresponds to `putMember(member: Member):` `Member`*

## Requirements

> The web server shall be based on raw socket calls, ie. no other middleware
is allowed.
The server endpoints (accepted urls) shall be dynamically extracted from the
content class using reflection.
Concurrency in the server should be handled using coroutines.
Data shall be communicated using JSON as protocol. You can use 3rd party
software as gson for this task, or write your own parser as we did in class

**A link to the github repository. In groups on [Peergrade](https://app.peergrade.io/login) by Wednesday
November 20th at 12:00**
