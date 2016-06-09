import pygtk

pygtk.require('2.0')


import gtk



class Application():

    

    def __init__(self):

        self.window = gtk.Window()

        self.window.set_title("Example GTK Application")

        

        self.create_widgets()

        self.connect_signals()

        

        self.window.show_all()

        gtk.main()

    

    

    def create_widgets(self):

        self.vbox = gtk.VBox(spacing=10)

        

        self.hbox_1 = gtk.HBox(spacing=10)

        self.label = gtk.Label("Your Name:")

        self.hbox_1.pack_start(self.label)

        self.entry = gtk.Entry()

        self.hbox_1.pack_start(self.entry)

        

        self.hbox_2 = gtk.HBox(spacing=10)

        self.button_ok = gtk.Button("OK")

        self.hbox_2.pack_start(self.button_ok)

        self.button_exit = gtk.Button("Exit")

        self.hbox_2.pack_start(self.button_exit)

        

        self.vbox.pack_start(self.hbox_1)

        self.vbox.pack_start(self.hbox_2)

        

        self.window.add(self.vbox)

    

    

    def connect_signals(self):

        self.button_ok.connect("clicked", self.callback_ok)

        self.button_exit.connect("clicked", self.callback_exit)

    

    

    def callback_ok(self, widget, callback_data=None):

        name = self.entry.get_text()

        print name

    

    

    def callback_exit(self, widget, callback_data=None):

        gtk.main_quit()

    


if __name__ == "__main__":

    app = Application()

