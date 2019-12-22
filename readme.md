## ADS Project Fall 2019

# Problem Description:

The aim of the project is to help manage the construction of buildings using a minheap and a red black tree. Construction can happen on only one building at a time. The building with the least executed time is worked upon till completion or till it has undergone construction for at least five days. If the building is not complete within five days, the building with the least executed time is picked from the heap and worked upon. Ties are broken by selecting the building with the lesser building number.

# Instructions to run the code:

The make file present in the submission folder would compile the code. Run the following command to execute the make file:
	make -f Makefile
After compilation using the make file, run the following command to run the program:
	java risingCity {file_name}
