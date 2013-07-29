SmartWetLab
===========

Repository of my work for SmartWetLab

RFIDLogger is a program which records data from a RFID reader using the Low Level Reader Protocol. The program utilizes
zeroconf networking and connects to a host name given in the program argument. Data is recorded until a "STOP" string
is entered into the console and is then printed to a file.

JmDNS is a Java implementation of zeroconf which I used in my RFID program, see http://jmdns.sourceforge.net/
