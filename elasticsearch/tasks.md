I 

1. Grab the data from here (zip with raw data, 1 java file with the format): 
https://drive.google.com/open?id=0Bx8vMZ0U_cKLOXRYYU43TFdLWlU
 or 
https://goo.gl/LtT3Sf

2. Take the sample data and write a piece of code that imports it to the Elasticsearch index.
3.  Select a sensible id from the available data and use it during indexing.

4.  Write code that does delete-by-query functionality. It should delete all documents that contain 
the specific term in given field.

5.  Take care when the field contains category indicator that should not be analyzed (i.e. 
„productClass”, „productSubclass”, „department”).

6. Import only a single xml file using the basic API (time!!!). If you are up to some extra work try 
using the Bulk API or Bulk Processor to upload all file contents efficiently:
• https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-bulk.html
• https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-bulk-processor.html

II You have to design a query to make the searches in your store.
 
Write a query that limits all the searches only to given product category. Lets say that user tries to search for a cell phone – 
limit the results accordingly (watch out for the quirks in the data that make it not so easy as it sounds).

LG, Samsung, Lenovo and Huawei have paid to promote their products in the store for every search performed. Add this campaign to the query.
Users sometimes might want to search not only on the names of the products but also on the descriptions as well. 
Make this change in the query you are creating.

User likes some colors more than the others. The best colors for the cellphones are white and gold. The black color is undesirable. Add this preferences to the query.
Now we have all the requirements covered. Lets make a nod to the customers and promote the cellphones with the high & reliable reviews that have a reasonable price.

Analytics

Create an aggregation that checks the user review statistics for three price ranges of the products. 
The ranges are below $10, between $10 and $50, and above $50. The statistic we are interested in is average customer review of the products in these price ranges.

The query from the previous task analyzed the whole timeline of our products. Using the initial product publication date split the review statistics into the quarterly buckets.
Prepare brand-specific analysis of our products. Select 2 brands to monitor (e.g. Samsung and Lenovo) and prepare the dedicated quarterly user opinion statistics for them.
Add the moving average, first and second derivative of the user opinion to the user review statistics and also to the brand-specific ones.

Functionalities a bit too large for a specific homeworkJust experiment! Some suggestions:.
Geo-spatial queries and aggregations on geonames dataset – maybe create some interesting metric (population vs elevation)? 
Play with more like this on bestbuy dataset.

Configure some stemming and cleanup for bestbuy dataset – at least do the examples from this presentation.

Download the logs from here: http://ita.ee.lbl.gov/html/contrib/NASA-HTTP.html. Use your own server logs if you have some. It is a bit hard to find open data set for apache server log that is recent.
- Import the logs using Logstash to Elasticsearch
- Properly set up the Kibana – refer to date field in the data (this is a time series after all)
- Use Kibana to visualize:
 Request count per day
 Metric – total data sent for the time period you have selected
 List the most frequently requesting users
 Show the breakdown of the request types (200, 404, etc.)

Put all visualizations into a single dashboard

Please first try to do it yourself – there are many articles about how to do it with everything already set up…
Experiment with more metrics!

