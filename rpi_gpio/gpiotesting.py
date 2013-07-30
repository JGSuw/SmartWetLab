#!/usr/bin/env python
import RPi.GPIO as GPIO
import time
import threading
import datetime

GPIO.setmode(GPIO.BCM)

class door_listener(threading.Thread):
    def run(self):
        
        """
        Commented out edge detenction implimentation because parity was occasionally flipping. Unreliable
        
        if GPIO.input(self.in_pin) == GPIO.HIGH:
            #Switch is initially open, output state is off
            state = 0
        else:
            #Switch is initially closed, output state is on
            state = 1
        while 1:
            GPIO.wait_for_edge(self.in_pin,GPIO.BOTH)
            if state == 1:
                #Switch from on to off, representing closed door
                print 'close'
                GPIO.output(self.out_pin,GPIO.LOW)
                state = 0
            elif state == 0:
                #Switch from off to on, representing door open
                print 'open'
                GPIO.output(self.out_pin,GPIO.LOW)
                state = 1
        pass
        """
        if GPIO.input(self.in_pin) == True:
            is_open= True
        elif GPIO.input(self.in_pin) == False:
            is_open = False
        
        self.is_running = True
        
        while self.is_running:
            if GPIO.input(self.in_pin) == True:
                GPIO.output(self.out_pin,True)
                if is_open != True:
                    is_open = True
                    print "Switch opened."
            elif GPIO.input(self.in_pin) == False:
                GPIO.output(self.out_pin,False)
                if is_open != False:
                    is_open = False
                    print "Switch Closed"
            time.sleep(1)
                
        
    def __init__(self,in_pin,out_pin):
        self.in_pin = in_pin
        self.out_pin = out_pin
        self.is_running = False
        threading.Thread.__init__(self)

    
    
if __name__ == '__main__':
    in_pin = int(raw_input("Enter input pin header (BCM): "))
    out_pin = int(raw_input("Enter output pin header (BCM): "))
    
    
    
    GPIO.setup(in_pin,GPIO.IN)
    GPIO.setup(out_pin,GPIO.OUT)
    
    print "initializing...."
    if GPIO.input(in_pin) == False:
        print 'Initially closed'
        GPIO.output(out_pin,False)
    elif GPIO.input(in_pin) == True:
        print 'Initially open'
        GPIO.output(out_pin,True)
    
    listener = door_listener(in_pin,out_pin)
    listener.start()
    
    raw_input("Enter anything to cease execution: ")
    listener.is_running = False
    print "goodbye"
