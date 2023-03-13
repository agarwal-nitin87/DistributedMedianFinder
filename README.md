# Instructions to execute

* Execute `mvn clean install` from package root.
* Execute `java -cp  target/my-app-1.0-SNAPSHOT.jar main.java.com.median.finder.Main` to start the program
* It will prompt for input file. Provide input file and program  will output the median in console.

# Algorithmic Approach
This approach assume that there are fixed range of integers. So, it uses the counting sort approach and calculates the frequency of every element in a reducer host.
Approximately, 5B possible integers in 32 bit space and use 64 bit long to store frequency. 1 KB can store 80 integer frequency. So, for 5 billion it requires ~ 60GB space.
So, all integer frequencies can fit on a single machine. 

Since, number of machines can also be large(~100K), the approach uses a middleware routing layer. 

![Distributed Median Finder](DistributedMedianFinder.png?raw=true "Distributed Median Finder")
