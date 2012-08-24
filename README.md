Vert.x-perf-test
================

This is a basic set of scripts to measure the performance of different aspects of Vert.x

The Event Bus
=============
The first set test aims to verify the performance of Vert.x event bus.

Sending a message to the bus (fire n forget mode)
-------------------------------------------------

The methodology that we will use is the following:
  * A Producer Verticle is sending a message to the bus to a specific destination
  * A Consumer Verticle is registered to receive messages from that specific destination
  * The Consumer will increment a counter every time a message is received.
  * The Consumer will take a sample of the current count and the elapsed time at regular intervals.
  * When the consumer doens't receive any new message for a few seconds, it will write the statistics into a file and terminate.

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
    


Sending a message to the bus and handle the response (Message/Reply mode)
-------------------------------------------------------------------------