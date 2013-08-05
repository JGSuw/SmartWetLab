#!/usr/bin/env python
import datetime
import json
import httplib, urllib
import basedevice

class refrigeratorRequestHandler(BaseDeviceRequestHandler):
    def do_cmd_test(self):
        self.send_response(200)
        self.send_header("content-type", "text/plain")
        self.end_headers()
        
        response = "path: " + self.path + "\n"
        response += "qs: " + str(self.query) + "\n"
        response += "post data: " + str(self.postdata) + "\n"
        response += "server_obj data: " + str(self.state) + "\n"
        response += "clined address: " + str(self.client_address) + "\n"
        
        self.wfile.write(response)
    
    def do_cmd_info(self):
        self.send_response( 200 )
        self.send_header('content-type', "text/plain")
        self.end_headers()
        info = {"uuid"  : self.state["uuid"],
                "status": "ready",
                "state" : "",
                "name"  : "refrigerator"}
        response = json.dumps(info)
        self.wfile.write(response)
        
    def do_cmd_acquire(self):
        try:
            self.state["tempwatcher"].run()
            self.state["doorwatcher"].run()
            self.send_response(200)
            self.send_header("content-type", "text/plain")
            self.end_headers()
            self.wfile.write("OK")
            self.state["SQUID-IP"] = self.client_address[0];
            self.state["SQUID-PORT"] = self.query["port"]
        except Exception:
            #SOMETHING WENT WRONG! HO-LI-****
            self.send_response(500)
            self.send_header("content-type","text/plain")
            self.end_headers()
            self.wfile.write("Get Joey to fix it")
        
    
        
class refrigerator(BaseDevice):
    def __init__(self, handler, settings,door_input_pin):
        basedevice.BaseDevice.__init__(self, handler)
        self.settings = settings
        self.state["doorwatcher"] = doorwatcher(24,
                                    self.state["uuid"],self.state["SQUID-IP"], self.state["SQUID-PORT"])
        self.state["tempwatcher"] = TemperatureWatcher(0,
                                    self.state["uuid"],self.state["SQUID-IP"],self.state["SQUID-PORT"])
            
    def update_time_forever(self):
        self.state["time"] = time.time()
        time.sleep(1)      
        
if __name__ == '__main__':
    in_pin = int(raw_input("Enter input pin header (BCM): "))
    dev = refrigerator(RefrigeratorRequestHandler,None,24)
    raw_input("Enter anything to cease execution: ")
    self.state["doorwatcher"].stop()
    self.state["tempwatcher"].stop()
    print "goodbye"
        

        
        
    
    
    

 
