import paho.mqtt.client as mqtt

#Fields for the connection status
_CONNECTED = "Connected"
_DISCONNECTED = "Disconnected"
_WAITING = "Waiting "

topic_id = "sensors/#"

class myMQTTsubscriber():
	#MQTT Parameters
	server_url = "192.168.0.15"
	client= mqtt.Client(client_id="secure_pi_central")


	#Attempt to connect
	def start_connection(self):
		self.client.connect(self.server_url, 1883, 30)
		#Start mqtt client
		self.client.loop_forever()

	#Set the IP address and try to connect again
	def set_IP_address(self, url):
		self.server_url = url
		#self.start_connection()

	def set_connection_params(self, on_connect, on_message, on_disconnect):
		#Connect client
		self.client.on_connect = on_connect
		self.client.on_message = on_message
		self.client.on_disconnect = on_disconnect
		#client.connect(server_url, 1883, 60)
		
