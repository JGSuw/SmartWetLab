#!/usr/bin/env python
import RPi.GPIO as GPIO
import time
import threading
import datetime

GPIO.setmode(GPIO.BCM)

class door_listener(threading.Thread):
    def run(self):
        now = datetime.datetime.now()
        while 1:
            GPIO.wait_for_edge(self.in_pin,GPIO.BOTH)
            if GPIO.output(self.out_pin) == GPIO.HIGH:
                print 'close'
                GPIO.output(self.out_pin,GPIO.LOW)
            else:
                print 'open'
                GPIO.output(self.out_pin,GPIO.LOW)
        pass
    def __init__(self,in_pin,out_pin):
        self.in_pin = in_pin
        self.out_pin = out_pin
        threading.Thread.__init__(self)
        pass
    def stop(self):
        GPIO.cleanup()
        threading.Thread.__stop()
        pass
    
    
    
if __name__ == '__main__':
    in_pin = int(raw_input("Enter input pin header (BOARD): "))
    out_pin = int(raw_input("Enter output pin header (BOARD): "))
    
    
    
    GPIO.setup(in_pin,GPIO.IN)
    GPIO.setup(out_pin,GPIO.OUT)
    
    if GPIO.input(in_pin) == GPIO.LOW:
        print 'Initially open'
        GPIO.output(out_pin,GPIO.HIGH)
    else:
        print 'Initially closed'
        GPIO.output(out_pin,GPIO.LOW)
    
    listener = door_listener(in_pin,out_pin)
    listener.start()
    
    raw_input("Enter anything to cease execution: ")
    listener.stop()
   
