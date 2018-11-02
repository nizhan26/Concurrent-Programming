# Concurrent-Programming
Concurrency is a status that multiple threads are excuted simutaneously rather than one after another, which imporves the efficiency on exploiting the computation power of CPU.Scheduling different activities is the core of concurrent programming. Meanwhile we should avoid deadlocks happened in the whole process. Files in this repository provides concurrent solutions on some classic single-thread problems with the language Scala.
The basis of Scala is realized by JVM, therefore, there are some similar methods on multi-threads control between Scala and Java. But Scala is more abstracted and functional. In addition, we also use the classes in Communication Scala Objects(CSO) libraries(by Bernard SUFRIN)to realize certain functions.


Sleeping Tutor
The tutor starts to teach only after both students have arrived;
The students leave only after the tutor ends the tutorial.
Methods: wait(), notify(), notifyall(), boolean semaphores

Cellular Automata
Implement the Game of Life, use 4/8 processes to update the cells on each generation.
The key is all processes should wait for others to finish before proceed to next step;
Methods: barrier() 

URL Web Crawler
It's like bfs, but is executed by the coordination of a controller and many workers, communitation is done by a structure named channel in CSO;
Methods: channel

Hamming Numbers:
• 1 is a Hamming number.
• If h is a Hamming number, then so are 2h, 3h and 5h.
Implement a network that continusly output the hamming numbers by channel;
Methods: channel





