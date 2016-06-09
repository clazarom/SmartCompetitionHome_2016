import paho.mqtt.client as mqtt
import console_interface as console
import datetime

timeout = 0.1 #seconds

#The callback for when the client receives a CONNACK response from the server
def on_connect(client, userdata, flags, rc):
    #print("Connected with result "+str(rc))

    #Subscribing in on_connect() means that if we lose the connection and
    #reconnect then subscriptions will be renewed
    client.subscribe(topic_id)

    #Update interface
    console.conn_status(screen, rc)
    #Check if there is any user input
    if (console.get_char(screen)):
        client.disconnect()
        print("Disconnect")

def on_message(client, userdata, msg):
    #print(msg.topic+ " " + str(msg.payload))
    content = str (msg.payload)
    topic = str(msg.topic)
    #Evaluate message:
    #TEMPERATURE
    if (topic == "sensors/temperature"):
        console.update_values(screen, console.TEMP_POSITION, content)
    elif (topic == "sensors/devTemp"):
        console.update_values(screen, console.TEMP_POSITION + 1, content)
    elif (topic == "sensors/pressure" or content.find("Pa")!= -1):
        console.update_values(screen, console.PRES_POSITION, content)
    elif (topic == "sensors/altitude" or content.find("meters")!= -1):
        console.update_values(screen, console.ALT_POSITION, content)
    elif (topic == "sensors/door" or content.find("door")!= -1):
        console.update_values(screen, console.DOOR_POSITION, content)
    elif (topic == "sensors/warning" or content.find("warning")!= -1):
        time = datetime.datetime.now()
        time.isoformat()
        console.update_values(screen, console.ALARM_POSITION, "!! ALARM : "+ str(time))
    
    #console.update_values(screen, console.TEMP_POSITION, "11111")

    screen.refresh()

    #Check if there is any use input
    if (console.get_char(screen)):
        client.disconnect()
        print("Disconnect")


#Set parameters for mqtt
client= mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

server_url = "127.0.0.1"
topic_id = "sensors/#"
client.connect(server_url, 1883, 60)

#Init the console interface
screen = console.init_screen()
#Start mqtt client
client.loop_forever()
