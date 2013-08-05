import BaseHTTPServer
import SocketServer
import json
import uuid
import hashlib
import thread
from urlparse import urlparse, parse_qs


class BaseDeviceRequestHandler(BaseHTTPServer.BaseHTTPRequestHandler):
    """A basic device with minimal functionality. A functional device
should extend this.
Devices should subclass BaseDevice and add do_cmd_<command> methods.
When An http request comes the query string is parsed into self.query,
which is a dictionary. Post data, if applicable, is stored as a string
in self.postdata while a parsed version (assuming its json data) is
stored in self.postdata_json. If either postdata or postdata_json do
not apply to the request the will have the value None.
do_cmd_<command> methods:
do_cmd_<command> methods are responsible for handling the response
headers and body and should always send the appropriate response code
for the conditions. an example info command response might look like
def do_cmd_info(self):
self.send_response( 200 )
self.send_header("content-type", "text/plain")
self.end_headers()
info = {"uuid" : self.state["uuid"],
"status" : "ready",
"state" : "",
"name" : "Example Device"}
response = json.dumps(info)
self.wfile.write(response)
"""
    
    def _do_always_first(self):
        self.state = self.server.state
        self.parsedurl = urlparse(self.path)
        self.query = parse_qs(self.parsedurl.query)
        #unlistify.
        self.query.update((k,v[0]) for k,v in self.query.items())
    
    def _do_always_last(self):
        #call the correct command handler
        try:
            cmd = getattr(self, "do_cmd_"+self.query["cmd"])
        except KeyError:
            self.send_response(400) #bad request
            self.send_header("content-type", "text/plain")
            self.end_headers()
            self.wfile.write("must specify a command")
        except AttributeError:
            self.send_response(400) #bad request
            self.send_header("content-type", "text/plain")
            self.end_headers()
            self.wfile.write("command " + self.query["cmd"] + " not found")
        else:
            cmd()
    
    def do_POST(self):
        self._do_always_first()
        length = int(self.headers['Content-Length'])
        self.postdata = self.rfile.read(length)
        try:
            self.postdata_json = json.loads(self.postdata)
        except ValueError:
            self.postdata_json = None
        self._do_always_last()
        
    def do_GET(self):
        self._do_always_first()
        self.postdata = None
        self.postdata_json = None
        self._do_always_last()
    


class BaseDevice:
    """ A basic device
The device should extend this class and define a new work method that
runs forever (or queues periodic jobs with timer.)
Also, An instance of this method shares the self.state dictionary with
every instance of Handler associated with it's http server.
"""

    def __init__(self,Handler,port=8000):
        self.state = {"uuid": self._generate_uuid(port)}
        self.httpd = SocketServer.TCPServer(("",port),Handler)
        self.httpd.state = self.state
    
    def _generate_uuid(self,port):
        #if the hardware address of the device can be found and
        # does not change then this should be deterministic.
        uniq_str = hex(uuid.getnode()<<16 | port)
        theuuid = uuid.UUID((hashlib.sha1(uniq_str).hexdigest())[0:32])
        return str(theuuid)
    
    def start(self):
        thread.start_new_thread( self.httpd.serve_forever,())
    
    def stop(self):
        self.httpd.socket.close()
        
    
if __name__ == '__main__':
    PORT = 8000
    bd = BaseDevice(BaseDeviceRequestHandler,PORT)
    
    bd.start()
    print "serving at port", PORT
    raw_input ("type something to stop: ")
    bd.stop()