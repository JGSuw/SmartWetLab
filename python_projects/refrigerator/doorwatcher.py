#!/usr/bin/env python
import RPi.GPIO as GPIO
import time
import threading
import datetime
import httplib, urllib
import json

"""
doorwatcher is a thread which monitors open/close events on the door and then posts
this data to the SQUID
"""
class DoorWatcher(threading.Thread):
    
    def __init__(self,in_pin,SQUID_IP,SQUID_PORT):
        self.in_pin = in_pin
        self.SQUID_IP = SQUID_IP
        self.SQUID_PORT = SQUID_PORT
        self.running = False
        GPIO.setup(in_pin,GPIO.IN)
        threading.Thread.__init__(self)

        
    def run(self):
        self.running = True
        self.__watch__(self)
    
    def __watch__(self):
        #Initialize state of the door
        if GPIO.input(in_pin) == True:
            is_open = True
        elif GPIO.input(in_pin) == False:
            is_open = False
            
        #Every second check the state of the door, and if its state has changed post the event to SQUID        
        while running == True:
            if GPIO.input(in_pin) == True:
                if not is_open == True:
                    self._post_squid_data({"event-type" : "opened","time" : datetime.time})
                    is_open = True
            elif GPIO.input(in_pin) == False:
                if not is_open == False:
                    self._post_squid_data({"event-type" : "closed","time" : datetime.time})
                    is_open == False
            time.sleep(1)
    
    def _post_squid_data(self,data):
        d = {"datum[uuid]": self.state["uuid"],
             "datum[data]": data}
        post_data = urllib.urlencode(d)
        headers = {"Content-type": "plain/text",
                   "Accept": "text/plain"}
        conn = httplib.HTTPConnection(self.SQUID_ID, \
                                      self.SQUID_PORT)
        conn.request("POST",post_data,headers)
    
    
    def stop(self):
        self.running = False
    
            
    
    

