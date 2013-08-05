#!/usr/bin/env python
import wiringpi2 as wiringpi
import threading
import time
import datetime
import math


#This subprogram monitors the temperature every minute and then returns the
#analog value of the temperature to the refrigerator program, which then writes the data to the SQUID.

#This subprogram reads data using the Raspberry Pi's SPI interface. The SPI interface returns
#a integer value between 0 and 1024, representing the ratio of the voltage drop across the thermometer
#to the 3.3 volt power supply. This interger value is then converted into a temperature value in celsius.




#I think this class will be a thread created by the refrigerator BaseDeviceRequestHandler, and the
#thermometer itself will post the data to the SQUID.


class tempwatcher(threading.Thread):
    
    def post(self):
        pass
    
    def run(self):
        self.is_running == True
        while self.is_running == True:
            values = spi.xfer2([1,8<<4,0])
            value = ((values[1]&3) << 8) + values[2]
            v = 3.31*value/1024
            x = ((self.R * v)/(3.31 - v))/self.R
            T = 1/(self.A1 + self.B1*math.log(x) + self.C1*math.pow(math.log(x),2) + self.D1*math.pow(math.log(x),3))
            self._post_squid_data({"temperature" : T,"time": datetime.time})
            sleep(10)
            
    def __init__(self,spi_channel,uuid,SQUID_IP,SQUID_PORT):
        #Device information and SQUID address
        self.is_running = False
        self.SQUID_IP = SQUID_IP
        self.SQUID_PORT = SQUID_PORT
        self.spi_channel = channel
        self.uuid = uuid
        threading.Thread.__init__(self)
        
        #Constants used to calculate temperature
        self.Rref = 10000
        self.R = 25500
        self.A1 = 3.354016e-3
        self.B1 = 2.569850e-4
        self.C1 = 2.620131e-6
        self.D1 = 6.383091e-8
        threading.Thread.__init__(self)
    
    def _post_squid_data(self,data):
        d = {"datum[uuid]": self.uuid,
             "datum[data]": data}
        post_data = urllib.urlencode(d)
        headers = {"Content-type": "plain/text",
                   "Accept": "text/plain"}
        conn = httplib.HTTPConnection(self.SQUID_ID, \
                                      self.SQUID_PORT)
        conn.request("POST",post_data,headers)   
