# JuiceBottler
CS-410 Lab 1

The purpose of this program was to fulfill the requirements of Lab 1 for CS-410 Operating Systems. 

The main thread creates fruit processing plants to make juice. 
Each plant has 4 workers that take care of a different step in the juice bottling process.

The steps are fetching the orange, peeling the orange, squeezing the orange, and bottling the juice.
The first thread, the fetcher, creates an orange, fetches the orange, then passes the orange to the peeler.
The peeler checks to see if he has an orange to peel, if so he peels it and passes it to the squeezer.
The squeezer checks to see if he has an orange, then squeezes it and passes it to the bottler.
The bottler checks to see if he has an orange, then bottles it and puts it in the "done" pile. There are 4 oranges in a bottle of juice.

When the "working hours" have ended, the plants shut down their workers and count how many bottles of juice they have as well as
how many oranges they wasted. 
