import gi
gi.require_version('Gtk', '3.0')
from gi.repository import Gtk, GObject
import threading
import mqtt_sub
from array import *
from operator import itemgetter
import errno
from socket import error as socket_error

import read_db as db


#import pygtk
#pygtk.require('2.0')
#import gtk as anotherGTK

import sys_keys as key
import gauge as temp_plot



#Example of GNOME PYTHON
class MyWindow(Gtk.Window):
	
	gui_started = False

	stopped = False
	#Fields for the connection status
	subscriber_ref = mqtt_sub.myMQTTsubscriber()
	conn_status = mqtt_sub._WAITING
	connection_label = Gtk.Label(label = "CONNECTION STATE: "+mqtt_sub._WAITING)
	IP_address = ""
	IP_label = Gtk.Label(label = "to   IP   0.0.0.0")
	entry = Gtk.Entry()

	temp_image = Gtk.Image()
	frame_temp = Gtk.Frame()


	#Fields for the sensors
	pressure_value = Gtk.Label(label = "---")
	altitude_value = Gtk.Label(label = "---")
	door_value =Gtk.Label(label = "---")
	alarm_value =Gtk.Label(label = "         ")

	#Fields for the competition system
	competition_table = Gtk.Table(rows=5, columns=3, homogeneous=False)

	roommate_first = Gtk.Label(label = "---")
	roommate_second = Gtk.Label(label = "---")
	roommate_third = Gtk.Label(label = "---")
	roommate_forth = Gtk.Label(label = "---")

	progressbar_first = Gtk.ProgressBar()
	progressbar_second = Gtk.ProgressBar()
	progressbar_third = Gtk.ProgressBar()
	progressbar_forth = Gtk.ProgressBar()

	distance_first = Gtk.Label(label = "---")
	distance_second = Gtk.Label(label = "---")
	distance_third = Gtk.Label(label = "---")
	distance_forth = Gtk.Label(label = "---")

	first_column = [roommate_first, roommate_second, roommate_third, roommate_forth]
	second_column = [progressbar_first, progressbar_second, progressbar_third, progressbar_forth]
	third_column = [distance_first, distance_second, distance_third, distance_forth]

	roommate_array = ["Adnan", "Cat", "Chris", "Sergio"]
	distance_array = array('i', [0, 0, 0, 0])

	winner_list = [(roommate_array[0], distance_array[0]), (roommate_array[1], distance_array[1]), (roommate_array[2], distance_array[2]), (roommate_array[3], distance_array[3])]


	#CALLBACKS: 
	#On key press
	def on_key_function(self, widget, event):
		if event.keyval == key.Escape:
			self.unfullscreen()
			self.maximize()
		elif event.keyval == key.Delete:
			self.stopped = True
			Gtk.main_quit
	
	def disconnect_gtk(self, widget,data=None):
		self.stopped = True
		print "delete event occurred"
		Gtk.main_quit()

	#Fake 'Press enter'
	def pressed_enter(self, widget):
		event = Gtk.gdk.Event(gtk.gdk.KEY_PRESS)
 		event.keyval = gtk.keysyms.Return
 		#event.time = 0 # assign current time
		self.emit('key_press_event', event)

	#On edited text
	def enter_callback(self, widget, edit):
		print "Entry contents: %s\n" % widget.get_text()
		if self.conn_status == mqtt_sub._CONNECTED:
			#Do nothing
			print("Already connected")
		else:
			self.IP_address = widget.get_text()
			label_text =  "to  IP  " +str(self.IP_address)
			self.set_label_value(self.IP_label , label_text)
			#Try to reconnect MQTT Subscriber
			self.subscriber_ref.set_IP_address(self.IP_address)
			try:
				self.subscriber_ref.start_connection()

			except socket_error as serr:
				self.set_connection_status( value=mqtt_sub._DISCONNECTED)
				print("Socket error")



	#On click action function
	def on_button_clicked_log(self, widget):
		print("Open old alarm and door logs")
	#On click action function
	def on_button_clicked_history(self, widget):
		print("Open past month of competition")

	#Create window
	def __init__(self, url, mqtt):

		#Update parameters
		self.subscriber_ref = mqtt
		self.IP_address = url
		self.IP_label = Gtk.Label(label = "to   IP   "+ self.IP_address)
		#WINDOW definition

		#Values for the boxes inside the window
		padding = 2
		spacing = 10
		margin_top = 2
		margin_left = 10
		expand = False
		fill = False

		Gtk.Window.__init__(self, title = "Automated Home")
		#self.set_default_size (300, 300)
		#self.maximize()
		self.fullscreen()
		self.set_border_width(5)
		#Add close event in the window
		self.connect("delete_event", self.disconnect_gtk)
		self.connect('key-press-event', self.on_key_function)
		
		#Make outer box - wrapper
        	outerBox = Gtk.VBox(False, padding)
		outerBox.set_spacing(spacing)
        	self.add(outerBox)


		# INNER FRAME 1 - CONNECTION
		frame = Gtk.Frame()
		vbox = Gtk.VBox(False, padding)
		#Status box
		hbox = Gtk.HBox (False, padding)
		hbox.pack_start(self.connection_label, expand, fill, padding)
		hbox.pack_start(self.IP_label, expand, fill, padding)
		vbox.pack_start(hbox, expand, fill, padding)
		#Changes box
		hbox = Gtk.HBox (False, padding)
		self.entry.set_max_length(15)
		self.entry.set_text(self.IP_address)
		self.entry.connect("activate", self.enter_callback, self.entry)
		hbox.pack_start(self.entry, expand, fill, padding)
		vbox.pack_start(hbox, expand, fill, padding)

		frame.add(vbox)
		frame.set_label("CONNECTION STATUS")
		outerBox.pack_start(frame, expand, fill, padding)


		#INNER FRAME 2 - AUTOMATED HOUSE
		frame = Gtk.Frame()
		hor_box = Gtk.HBox(expand, fill,  padding)

		#I. Temperature, Pressure and altitude
		#hbox = Gtk.HBox(expand, fill, padding)
		
		#Temperature
		tbox = Gtk.VBox(expand, fill, padding)
		#temp_plot.generate_temp_fig(0)
		self.temp_image.set_from_file("temp_plot.png")
        	self.frame_temp.add(self.temp_image)
		tbox.pack_start(self.frame_temp, False, False, margin_top )
		hor_box .pack_start(tbox, False, False, margin_left )

		#Pressure and altitute
		tbox = Gtk.VBox(expand, fill, padding)
		#Pressure
		innerbox = Gtk.HBox(expand, fill, padding)
		label =  Gtk.Label(label = "Pressure: ")
		innerbox.pack_start(label, False, False, margin_top )
		innerbox.pack_start(self.pressure_value, False, False, margin_top )
		tbox.pack_start(innerbox, False, False, margin_top )

		#Altitude
		innerbox = Gtk.HBox(expand, fill, padding)
		lab =  Gtk.Label(label = "Altitude: ")
		innerbox.pack_start(lab, False, False, margin_top )
		innerbox.pack_start(self.altitude_value, False, False, margin_top )
		tbox.pack_start(innerbox, False, False, margin_top )

		hor_box .pack_start(tbox, False, False, margin_left )
		#vbox.pack_start(hbox, False, False, margin_left )

		#I. Door and alarm
		vertical_box = Gtk.VBox(expand, fill, padding)

		#Door
		hbox = Gtk.HBox(expand, fill, padding)
        	lab = Gtk.Label(label = "Door: ")
		hbox.pack_start(lab, False, False, margin_left )
		hbox.pack_start(self.door_value, False, False, margin_left )
		vertical_box.pack_start(hbox, False, False, margin_left )

		#Alarm
		hbox = Gtk.HBox(expand, fill, padding)
		hbox.pack_start(self.alarm_value, False, False, margin_left )
		vertical_box.pack_start(hbox, False, False, margin_left )

		#Log button
		hbox = Gtk.HBox(expand, fill, padding)
		self.button = Gtk.Button(label = "Logs")
		self.button.connect("clicked", self.on_button_clicked_log)
		hbox.add(self.button)
		vertical_box.pack_start(hbox, False, False, margin_top )

		hor_box .pack_start(vertical_box, False, False, margin_left )

		frame.add(hor_box )
		frame.set_label("HOUSE KEEPING")
		outerBox.pack_start(frame, expand, fill, padding)

		#INNER FRAME 3--- COMPETITION HOME
		frame = Gtk.Frame()
		vbox = Gtk.VBox(expand, fill,  padding)
		#Make a table
		tbox = Gtk.VBox(expand, fill,  padding)
		self.competition_table = Gtk.Table(rows=5, columns=3, homogeneous=False)
		self.competition_table = self.init_comp_table(self.competition_table)
		self.reorder_table()
		tbox.add(self.competition_table)
		vbox.add(tbox)

		#History button
		bbox = Gtk.VBox(expand, fill,  padding)
		self.button = Gtk.Button(label = "History")
		self.button.connect("clicked", self.on_button_clicked_history)
		bbox.add(self.button)
		vbox.add(bbox)
		frame.add(vbox)

		frame.set_label("COMPETITION")
		outerBox.pack_start(frame, expand, fill, padding)

		#self.reorder_table()

	

	#Initialize competition table
	def init_comp_table(self, table):
		#First column
		lab = Gtk.Label(label = "ROOMIE: ")
		table.attach(lab, 0, 1, 0, 1)
		#lab.set_justify(Gtk.JUSTIFY_LEFT)
		table.attach(self.first_column[0], 0, 1, 1, 2)
		#lab.set_justify(Gtk.JUSTIFY_LEFT)
		table.attach(self.first_column[1], 0, 1, 2, 3)
		#lab.set_justify(Gtk.JUSTIFY_LEFT)
		table.attach(self.first_column[2], 0, 1, 3, 4)
		table.attach(self.first_column[3], 0, 1, 4, 5)
		#lab.set_justify(Gtk.JUSTIFY_LEFT)

		#Second column		
		lab = Gtk.Label(label = " ")
		table.attach(lab, 1, 2, 0, 1)
		table.attach(self.second_column[0], 1, 2, 1, 2)
		table.attach(self.second_column[1], 1, 2, 2, 3)
		table.attach(self.second_column[2], 1, 2, 3, 4)
		table.attach(self.second_column[3], 1, 2, 4, 5)
		#Third column
		lab = Gtk.Label(label = "DISTANCE (Km)")
		table.attach(lab, 2, 3, 0, 1)
		table.attach(self.third_column[0], 2, 3, 1, 2)
		table.attach(self.third_column[1], 2, 3, 2, 3)
		table.attach(self.third_column[2], 2, 3, 3, 4)
		table.attach(self.third_column[3], 2, 3, 4, 5)
		return table


	def main(self):
        	# All PyGTK applications must have a gtk.main(). Control ends here
        	# and waits for an event to occur (like a key press or mouse event).
        	Gtk.main()
	
	#Update competition table
	def update_distance_value(roommate, distance):
		self.distance_array[roommate] = distance
		self.reorder_table()

	def reorder_table(self):
		#sorted(self.winner_list, key=lambda x: x[1])
		self.winner_list = sorted(self.winner_list, key = itemgetter(1), reverse=True)
		#Attach to table again
		index = 0
		max_bar = self.getDistance(self.winner_list[index])
		fraction = 0.0
		for (room, distance) in self.winner_list:
			print("Index: " + str(index) +" " + str(distance))
			#Roommate update:
			self.set_label_value( self.first_column[index], room)
			#Progress bar column:	
			fraction = float(distance)/float(max_bar)
			print (str(fraction))
			self.second_column[index].set_fraction(fraction)
			#Distance column:
			self.set_label_value( self.third_column[index], distance)
			index += 1

		
	def getDistance(self, item):
		return item[1]
				
		
	#Function to update temperature values
	def set_temp_value(self, temp):
		print(str(temp))
		#fig = temp_plot.generate_temp_fig(int(float(temp)))
		#self.temp_image.set_from_file("temp_plot.jpeg")
        	#self.frame_temp.add(self.temp_image)

		

	#Function to update label values
	def set_label_value(self, label, value):
		label.set_text(str(value))

		#Use it to also verify competition database:
		for (roomie_name) in roommate_array:
                        last_row = db.read_last_sample(roommie_name)
                        monhtly_distance = last_row[3]
                        update_distance_value(roomie_name, monthly_distance)

	#Functin to update connection status
	def set_connection_status(self, value):
		self.set_label_value(self.connection_label, "CONNECTION STATE: "+value)
		self.conn_status = value
		if value == mqtt_sub._CONNECTED:
			print("Connected")
			self.entry.set_editable(False)
		else:
			print("Not connected")
		



