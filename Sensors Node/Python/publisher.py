import paho.mqtt.client as mqtt
import Adafruit_TMP.TMP006 as TMP006
import door_controller as DOOR
import MPL3115A2 as MPL3115A2
import time

#The callback for when the client receives a CONNACK response from server
def on_connect(client, userdata, flags,rc):
	print("Connected with result code "+ str(rc))

#Read sensor data
def read_door_state():
	return 1

client = mqtt.Client()
client.on_connect = on_connect

server_url = "192.168.0.103"
client.connect(server_url, 1883, 60)
client.loop_start()

id = "sensors"
d_topic = "door"
t_topic = "temperature"
p_topic = "pressure"
a_topic = "altitude"
w_topic = "warning"

temp_add = 0x40
temp_bus = 1
temp_sensor =  TMP006.TMP006(address = temp_add, busnum=temp_bus)
#time.sleep(1.0)
temp_sensor.begin(samplerate = TMP006.CFG_16SAMPLE)

pres_sensor = MPL3115A2
pres_sensor.calibrate()

door_sensor = DOOR

#Loop to read and publish
while True:
	#Read sensors
	#DOOR
	door = door_sensor.check_door()
	door_state = "unknown"
	if door == 0:
		door_state = "closed"
	elif door == 1:
		door_state = "open"
	print("Door: "+ door_state +" - "+  str(door))
	client.publish(id+"/"+d_topic,str(door) +" "+door_state)
	#WARNING
	warning = door_sensor.check_inc(1)
	if warning == 0:
		client.publish(id+"/"+w_topic, "!!!")
		print ("!Warning: "+ str(warning))
	#TEMPERATURE
	obj_temp = temp_sensor.readObjTempC()
	die_temp = temp_sensor.readDieTempC()
	client.publish(id+"/"+"devTemp", str(obj_temp) +" C")
	client.publish(id+"/"+t_topic, str(die_temp)+ " C")
	#PRESSURE
	pres_sensor.calibrate()
	pressure = pres_sensor.pressure()
	altitude = pres_sensor.altitude()/100
	client.publish(id+"/"+p_topic, str(pressure)+ " Pa")
	client.publish(id+"/"+a_topic, str(altitude)+" meters")
	time.sleep(1.0)
