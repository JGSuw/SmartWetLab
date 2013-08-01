#!/usr/bin/env python
import wiringpi2 as wiringpi
import threading
import time


#This subprogram monitors the temperature every minute and then returns the
#analog value of the temperature to the refrigerator program, which then writes the data to the SQUID.

#This subprogram reads data using the Raspberry Pi's SPI interface. The SPI interface returns
#a integer value between 0 and 1024, representing the ratio of the voltage drop across the thermometer
#to the 3.3 volt power supply. This interger value is then converted into a temperature value in celsius.




#I think this class will be a thread created by the refrigerator BaseDeviceRequestHandler, and the
#thermometer itself will post the data to the SQUID.


class TempWatcher(threading.Thread):
    
    def post(self):
        pass
    
    #First version is going to read every second. TODO: change to read every minute and post to SQUID
    def run(self):
        self.is_running == True
        wiringpi.wiringPiSetup()
        wiringpi.mcp3442Setup(0)
        while self.is_running == True:
            'Read from pin 10 (MOSI SPI pin)'
            current_voltage = wiringpi.analogRead(10)
            print 'Voltage read: ' + current_temp
            time.sleep(1)
            
    def __init__(self, state,spi_channel):
        self.is_running = False        
        self.spi_channel = channel
        #The state object from the basedevice and request handler, do not mess with it!
        self.state = state
        
        
        threading.Thread.__init__(self)
        
     
"""Experimental program entrypoint for testing"""
if __name__ == '__main__':
    temp_watcher = TempWatcher(None,0)
    temp_watcher.run()
    
    raw_input("Enter anything to cease execution: ")
    temp_watcher.is_running = False
    print "goodbye"    
