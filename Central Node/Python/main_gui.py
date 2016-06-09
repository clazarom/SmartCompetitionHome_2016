import gi
gi.require_version('Gtk', '3.0')
from gi.repository import Gtk

#Example of GNOME PYTHON
class MyWindow(Gtk.Window):
	def __init__(self):
		Gtk.Window.__init__(self, title = "Automated Home")
		self.set_default_size (300, 300)

		#Make th box
		vbox = Gtk.VBox(False, 5)
        	outerBox = Gtk.VBox(False, 5)
		#self.add(vbox)
        	self.add(outerBox)
        	outerBox.pack_start(vbox, False, False, 0)
        	self.set_border_width(5)

		#First sections --- Automated house
		#Label - Connection
		frame = Gtk.Frame()
        	label = Gtk.Label(label = "Temperature: ")
        	frame.add(label)
        	vbox.pack_start(frame, False, False, 0)
		
		frame = Gtk.Frame()
        	label = Gtk.Label(label = "Pressure: ")
        	frame.add(label)
        	vbox.pack_start(frame, False, False, 0)

		frame = Gtk.Frame()
        	label = Gtk.Label(label = "Altitude: ")
		label2 = Gtk.Label(label = "Altitude: ")
        	frame.add(label)
        	vbox.pack_start(frame, False, False, 0)

		frame = Gtk.Frame()
        	label = Gtk.Label(label = "Door: ")
        	frame.add(label)
        	frame.add(label2)
        	vbox.pack_start(frame, False, False, 0)

		self.button = Gtk.Button(label = "ON")
		self.button.connect("clicked", self.on_button_clicked)
		vbox.add(self.button)

		#Second section --- Rommate of the week points
		#Make a table

	
	#On click action function
	def on_button_clicked(self, widget):
		print("Hello world")

#Create an instance of MyWindow
win = MyWindow()
#Include delete button
win.connect("delete-event", Gtk.main_quit)
win.show_all()
#Start GTK loop to be closed when windows is closed
Gtk.main()
