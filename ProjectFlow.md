# The flow of the Data Collection Framework

### The main class : <code>JuliusCaesar</code>

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
* Initialize a jobtoken system so that only a set of jobs run at a given time and they wait for a while before executing a new set of jobs from the job stack
* The executor executes the thread which takes an instance of runnable class <code>MrRunnable</code>


### Runnable Class : <code>MrRunnable</code>

* This class contains the run method for the threads.
* This run method calls getTweets of SearchTrends class.

### Get Trends class : <code>Trends

* This class get trends per different locations , uses jost 1 app as this is called per day.

### Search Trends Class : <code>SearchTrends</code>

* This class contains the getTweets method that is used to return tweets by making API call using the search API.
* The search api returns a json and this is directly stored in the postgres table

### Consumer Pool Class: <code>ConsumerPool</code>

* This class creates the consumer objects provided the credentials.
* Per twitter app i have created 80 objects so that 80 threads can be fired at once.
* Returns a BlockingQueue so that the objects are picked up to do the job and added back to pool after that job is completed

### MrTimer class : <code>MrTimer</code>

* Schedules a task at particular time and then runs that task after the provided interval.
* Has a run method that runs at a particular time

### MrMestriClass : <code>MrMestri</code>

* This class builds a stack of jobs for a given day that is refreshed everyhour. 
* The number of jobs put to the stack depends on the number of apps and the number of search calls that  can be made per hour.

