import curses
from std_operations import _get_char

TEMP_POSITION = 3
PRES_POSITION = 5
ALT_POSITION = 6
DOOR_POSITION = 7
ALARM_POSITION = 8

def init_screen():
    #Init the terminal interface
    screen = curses.initscr()
    curses.noecho()
    curses.curs_set(0)
    screen.keypad(1)

    #Output strings
    top_pos = 0
    left_pos = 0
    screen.addstr(top_pos, left_pos, "SMART HOUSE DATA:")

    #Close box
    top_pos = TEMP_POSITION - 1
    left_pos = 0
    screen.addstr(top_pos, left_pos, "-------------------------------------------")

    #Temperature
    top_pos = TEMP_POSITION
    left_pos = 1
    screen.addstr(top_pos, left_pos, "Temperature")
    left_pos = 17
    screen.addstr(top_pos, left_pos, "|")
    left_pos = 20
    screen.addstr(top_pos, left_pos, " ---")
    top_pos = PRES_POSITION
    left_pos = 1
    ######
    top_pos = TEMP_POSITION + 1
    left_pos = 1
    screen.addstr(top_pos, left_pos, "Raspi Temp")
    left_pos = 17
    screen.addstr(top_pos, left_pos, "|")
    left_pos = 20
    screen.addstr(top_pos, left_pos, " ---")
    #Pressure
    top_pos = PRES_POSITION
    left_pos = 1
    screen.addstr(top_pos, left_pos, "Pressure")
    left_pos = 17
    screen.addstr(top_pos, left_pos, "|")
    left_pos = 20
    screen.addstr(top_pos, left_pos, " ---")
    #Altitude
    top_pos = ALT_POSITION
    left_pos = 1
    screen.addstr(top_pos, left_pos, "Altitude")
    left_pos = 17
    screen.addstr(top_pos, left_pos, "|")
    left_pos = 20
    screen.addstr(top_pos, left_pos, " ---")
    #Door
    top_pos = DOOR_POSITION
    left_pos = 1
    screen.addstr(top_pos, left_pos, "Door")
    left_pos = 17
    screen.addstr(top_pos, left_pos, "|")
    left_pos = 20
    screen.addstr(top_pos, left_pos, " ---")
    #Alarm
    top_pos = ALARM_POSITION
    left_pos = 1
    screen.addstr(top_pos, left_pos, "Alarm")
    left_pos = 17
    screen.addstr(top_pos, left_pos, "|")
    left_pos = 20
    screen.addstr(top_pos, left_pos, " ---")
    #Close box
    top_pos = ALARM_POSITION + 1
    left_pos = 0
    screen.addstr(top_pos, left_pos, "-------------------------------------------")

    top_pos = ALARM_POSITION + 2
    left_pos = 0
    screen.addstr(top_pos, left_pos, "Press 'q' to exit")
    return screen

def conn_status(screen, con):
    top_pos = 1
    left_pos = 0
    screen.addstr(top_pos, left_pos, "Connected:  " + str(con))
    screen.refresh


def get_char(screen):
    #event = screen.getch()
    event = _get_char()
    if event == 'q':
        #End terminal
        curses.echo()
        screen.keypad(0)
        curses.endwin()
        return True
    else:
        return False

def update_values(screen, top, val):
    left_pos = 20
    screen.addstr(top, left_pos, str(val)+ "            ")
    screen.refresh


    


