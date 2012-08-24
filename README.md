Vert.x Performance Suite
========================

This is a basic set of scripts to measure the performance of different aspects of Vert.x

Throughput - The Event Bus 
==========================
The first set test aims to verify the performance of Vert.x event bus.


Sending a message to the bus (fire n forget mode)
-------------------------------------------------

The methodology that we will use is the following:
  * A Producer Verticle is sending messages to the bus to a specific destination
  * A Consumer Verticle is registered to receive messages from that specific destination
  * The Consumer will increment a counter every time a message is received.
  * The Consumer will take a sample of the current count and the elapsed time at regular intervals.
  * When the consumer doens't receive any new message for a few seconds, it will write the average throughtput into a file and terminate.

With this simple pattern we will try to test the performance of sending Numbers (long), Strings and JSonObjects.

    +--------------+
    |   Producer   |
    |--------------|
    | bus.send()   |
    +--------------+
          |
          | destination
          V
    +--------------+
    |   Consumer   |
    |--------------|
    | +count       |
    +--------------+
    
We will test this simple setup in a number of different configurations, such as:

  * Type Variation
    * send a number
    * send a string
    * send a JsonObject
  * Producer Variation
    * Trying to increase the number of producers and keep only one consumer
  * Consumer Variation
    * Trying to increase the number of consumers and keep only one producer
  * Symmetric Variation
    * Trying to increase both Producers and Consumers

**NOTE:**
  - All tests in this section will use 1 single JVM (no cluster mode)
  - We will sample CPU and GC to understand load and GC impact


Sending a message to the bus and handle the response (Message/Reply mode)
-------------------------------------------------------------------------
In this case we want to measure the throught of the bus when sending a message
that expect a response (async). (TODO)

