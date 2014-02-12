#Summarization of Unstructured Data
------------------------------------------
###Findings:

Brief Paper on summarization of unstructured data:
[http://nlp.cs.rpi.edu/paper/ispn.pdf](http://nlp.cs.rpi.edu/paper/ispn.pdf "Summarization of unstructred data")
	
	What this paper talks about is how we can use clustering , ranking of these clusters
	to find the right ones to use for summarization. We can also use measures like
	co-occurence to derive the summary.

### Fields i need from the tweet:

	Text,geo,source,created_at - I think for basic analysis this is sufficient.

###Things to do:
	
	1) How to use streaming API and spawn threads. When to stop logic?
	2) Should i use search / streaming api?
	3) Create MySQL database to hold trends and required data from tweets
	4) Create dump of trends and tweets on mongodb with all fields.
	5) Get a vbox with ubuntu.
	


	


