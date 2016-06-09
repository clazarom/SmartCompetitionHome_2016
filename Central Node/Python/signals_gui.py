import pygtk

pygtk.require('2.0')

import gtk

import time


class App:

    def __init__(self):

        self.window = gtk.Window(type=gtk.WINDOW_TOPLEVEL)

        self.hbox = gtk.HBox()

        self.button_print = gtk.Button(label='Print something')

        self.button_sleep = gtk.Button(label='Hang for 5 seconds')


        self.hbox.pack_start(self.button_print)

        self.hbox.pack_start(self.button_sleep)


        self.button_print.connect('clicked', self.print_hi)

        self.button_sleep.connect('clicked', self.sleep)

        self.window.connect('destroy', self.quit)

        

        self.window.add(self.hbox)

        self.window.show_all()


        gtk.main()


    def print_hi(self, widget, callback_data=None):

        print 'Hi there!'


    def sleep(self, widget, callback_data=None):

        time.sleep(5)


    def quit(self, widget, callback_data=None):

        gtk.main_quit()


if __name__ == "__main__":

    app = App()

