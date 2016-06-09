import gtk


# Define the callback functions first:

def exit(widget, callback_data=None):

  window.hide_all()

  gtk.main_quit()


window = gtk.Window()

entry = gtk.Entry()

button_ok = gtk.Button("OK")

button_cancel = gtk.Button("Cancel")


# Connect events to callback functions:

button_cancel.connect("clicked", exit)


vbox = gtk.VBox()

vbox.pack_start(entry)

hbox = gtk.HBox()

hbox.pack_start(button_ok)

hbox.pack_start(button_cancel)

vbox.pack_start(hbox)


window.add(vbox)

window.show_all()

# Put gtk.main() last so our callback functions are used.

gtk.main()
