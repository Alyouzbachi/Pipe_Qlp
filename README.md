# PIPE 2 #

A tool for creating and analysing Petri nets, migrated from http://pipe2.sourceforge.net/about.html

## About ##
PIPE2 is an open source, platform independent tool for creating and analysing Petri nets including 
Generalised Stochastic Petri nets. Petri nets are a popular way for modelling concurrency and synchronisation 
in distributed systems. To learn more about Petri nets, you can start by reading the 2006/7 MSc. 
project report available [here](http://pipe2.sourceforge.net/docs.html).

PIPE2 began life in 2002/3 as an MSc. Group Project at the Department of Computing, Imperial College London called 
"The Platform Independent Petri net Editor PIPE". This is now the official branch of that project. 
It is still being maintained as an on-going project at the college.

## QLP ##
QLP started in 2016 at the Department of Software Engineering,Damascus university.
QLP stands for Query language for petri nets.
QLP implemented the principal concept of Ltl(Linear temporal logic ). [here](http://www.dis.uniroma1.it/~liberato/ar/ltl/ltl.html).
QLP will transform petri nets to reachability graphs and traverse in them to inquiry the input queries on places and determine wether is true or false and why.

QLP Syntax Menu to inquiry on petri nets :

(X) Next, (G) globally,(F) finally,(U) until,(E) Exist to check whether a place is fount once at least once ,(A) all to check if this query always true,
D or deadlock to show any possible deadlocks,L or live for liveness,
AND,^,&,OR,|,Not,~,!:supported between operations.

Ex: E(aUp)  is there a route where a is true until p became true once.
All(a F b) OR !E(global d) is all routes for (a) will (b) be future or is not once d is global.
 

### Imperial College Projects ###
* 2002/3 James Bloom, Clare Clark, Camilla Clifford, Alex Duncan, Haroun Khan and Manos Papantoniou create PIPE
* 2003/4 Tom Barnwell, Michael Camacho, Matthew Cook, Maxim Gready, Peter Kyme and Michael Tsouchlaris 
continue the project as PIPE2 with substantial bug fixes and user interface enhancements.
* 2005 Nadeem Akharware adds advanced GSPN analysis capabilities.
* 2006/7 Edwin Chung, Tim Kimber, Benjamin Kirby, Will Master and Matt Worthington made bug fixes, 
code efficiency improvements and added zoom to the GUI and reachability graph generation capability.


## Installation ##
### Build jar ###
To build the ```jar``` library execute the following from within the PIPE root directory:

    $ mvn install
    
Once finished you should see the file ```target/PIPE-4.3.0.jar```.


### Execution ###
In order to run PIPE tool execute the following from within the PIPE root directory:

    $ mvn exec:exec
    
### Note: local libs ###
There are some internal libraries, which need to be found for maven. For the mean time they are located in the project under ``src/local-libs`` and this directory is treated as a local library. When installing expect the following warning:

	[WARNING] The POM for internal:XXX is missing, no dependency information available
	
The original method for these local libraries required running a Python script to install the local libraries using ``mvn install``, however this new method removes the need for a pre-install stp).

If you know of a better way to do this, please raise it in the issues section.
