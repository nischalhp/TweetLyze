# The flow of the Data Collection Framework

### The main class :

The main class is JuliusCaesar. This class controls the whole data collection process.
These are the following things i do in the above class:

* First Set the properties path
* Declare a consumerPool
* Set properties using the properties file path
* Set the logger
* Instantiate the consumer Pool using the method <code>ConsumerPool.buildConsumerPool()</code>
* Once that is done , i specify the time interval for the threads to wait to space all the search calls
* Build a executor service with specified threads
* Schedule the fetching of trends using the method <code>MrTimer.startTask()</code>
* Build a stack of jobs for a given time window that is 1 hour using the method <code>MrMestri.buildJobs</code>


