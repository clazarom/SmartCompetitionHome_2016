
import main_gui as gui
import datetime
import mqtt_sub as subscriber
import errno
from socket import error as socket_error


gui.GObject.threads_init()
timeout = 0.1 #seconds


#The callback for when the client receives a CONNACK response from the server
def on_connect(client, userdata, flags, rc):
    print("Connected with result "+str(rc))

    #Subscribing in on_connect() means that if we lose the connection and
    #reconnect then subscriptions will be renewed
    client.subscribe(subscriber.topic_id)
    #Start GTK loop to be closed when windows is closed
    win.set_connection_status( value= subscriber._CONNECTED)

    if not win.gui_started:
	gui_started = True
        thread = gui.threading.Thread(target=start_gui)
        thread.daemon = True
        thread.start()


#Callback for disconnection
def on_disconnect(client, userdata, rc):
    print("Client has beeeeen disconnected")

#Callback for received messages
def on_message(client, userdata, msg):
    if (win.stopped):
	client.disconnect()
	print("Disconnect")
    #print(msg.topic+ " " + str(msg.payload))
    content = str (msg.payload)
    topic = str(msg.topic)
    #Evaluate message:
    #TEMPERATURE
    if (topic == "sensors/temperature"):
	content_temp = content[:-2]
	win.set_temp_value(temp=content_temp)
    elif (topic == "sensors/devTemp"):
	print("Device temp:"+ str(content))
    elif (topic == "sensors/pressure" or content.find("Pa")!= -1):
	win.set_label_value(label=win.pressure_value, value=content)
    elif (topic == "sensors/altitude" or content.find("meters")!= -1):
	win.set_label_value(label=win.altitude_value, value=content)
    elif (topic == "sensors/door" or content.find("door")!= -1):
	time = datetime.datetime.now()
	time.isoformat()
	door_update = str(content) + "  " + str(time)
	win.set_label_value(label=win.door_value, value=door_update)
    elif (topic == "sensors/warning" or content.find("warning")!= -1):
	time = datetime.datetime.now()
	time.isoformat()
	alarm_update = "ALARM!  "+ str(time)
	win.set_label_value(label=win.door_value, value=alarm_update)
    
    #console.update_values(screen, console.TEMP_POSITION, "11111")


    #Check if there is any use input
    #if (console.get_char(screen)):
	#client.disconnect()
	#print("Disconnect")
def start_gui():
	#gui.Gtk.main()
	win.main()
	
#Subscriber
mqttSub = subscriber. myMQTTsubscriber()
mqttSub.set_connection_params(on_connect, on_message, on_disconnect)
gui_started = False

#Show window
#Init the GTK interface
#Create an instance of MyWindow
win = gui.MyWindow(mqttSub.server_url, mqttSub)
win.show_all()

#Try to connect MQTT Subscriber
try:
	mqttSub.start_connection()
except socket_error as serr:
	win.set_connection_status( value=subscriber._DISCONNECTED)
	print("Socket error")
	#Start gui thread:
	if not win.gui_started:
	    win.gui_started = True
            thread = gui.threading.Thread(target=start_gui)
            thread.daemon = True
            thread.start()
	    mqttSub.client.loop_forever()



	







