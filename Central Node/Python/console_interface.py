import curses
from std_operations import _get_char

TEMP_POSITION = 4
PRES_POSITION = 6
ALT_POSITION = 7
DOOR_POSITION = 8
ALARM_POSITION = 9

def simple_screen():
    stdscr = curses.initscr()
    screen = stdscr.subwin(23, 79, 0 ,0)
    screen.box()
    return screen

def init_screen():
    #Init the terminal interface
    stdscr = curses.initscr()
    screen = stdscr.subwin (23, 79, 0, 0)
    screen.box() 
    curses.noecho()
    curses.cbreak()
    #Cursor Invisible
    curses.curs_set(0)
    #screen.keypad(1)
    #Define top menu
    

    #Output strings
    top_pos = 1
    left_pos = 1
    screen.addstr(top_pos, left_pos, "SMART HOUSE DATA:")

    #Close box
    top_pos = TEMP_POSITION - 1
    left_pos = 1
    screen.addstr(top_pos, left_pos, "-------------------------------------------")

    #Temperature
    top_pos = TEMP_POSITION
    left_pos = 2
    screen.addstr(top_pos, left_pos, "Temperature")
    left_pos = 17
    screen.addstr(top_pos, left_pos, "|")
    left_pos = 20
    screen.addstr(top_pos, left_pos, " ---")
    top_pos = PRES_POSITION
    left_pos = 2
    ######
    top_pos = TEMP_POSITION + 1
    left_pos = 2
    screen.addstr(top_pos, left_pos, "Raspi Temp")
    left_pos = 17
    screen.addstr(top_pos, left_pos, "|")
    left_pos = 20
    screen.addstr(top_pos, left_pos, " ---")
    #Pressure
    top_pos = PRES_POSITION
    left_pos = 2
    screen.addstr(top_pos, left_pos, "Pressure")
    left_pos = 17
    screen.addstr(top_pos, left_pos, "|")
    left_pos = 20
    screen.addstr(top_pos, left_pos, " ---")
    #Altitude
    top_pos = ALT_POSITION
    left_pos = 2
    screen.addstr(top_pos, left_pos, "Altitude")
    left_pos = 17
    screen.addstr(top_pos, left_pos, "|")
    left_pos = 20
    screen.addstr(top_pos, left_pos, " ---")
    #Door
    top_pos = DOOR_POSITION
    left_pos = 2
    screen.addstr(top_pos, left_pos, "Door")
    left_pos = 17
    screen.addstr(top_pos, left_pos, "|")
    left_pos = 20
    screen.addstr(top_pos, left_pos, " ---")
    #Alarm
    top_pos = ALARM_POSITION
    left_pos = 2
    screen.addstr(top_pos, left_pos, "Alarm")
    left_pos = 17
    screen.addstr(top_pos, left_pos, "|")
    left_pos = 20
    screen.addstr(top_pos, left_pos, " ---")
    #Close box
    top_pos = ALARM_POSITION + 1
    left_pos = 1
    screen.addstr(top_pos, left_pos, "-------------------------------------------")

    top_pos = ALARM_POSITION + 2
    left_pos = 1
    screen.addstr(top_pos, left_pos, "Press 'q' to exit")
    screen.refresh()
    return screen

def conn_status(screen, con):
    top_pos = 2
    left_pos = 1
    screen.addstr(top_pos, left_pos, "Connected:  " + str(con))
    screen.refresh()


def get_char(screen):
    #event = screen.getch()
    event = _get_char()
    if event == 'q':
        #End terminal
        return True
    else:
        return False

def end_screen_win(screen):
    curses.nocbreak()
    screen.keypad(0)
    screen.refresh()
    curses.echo()
    curses.curs_set(1)
    curses.endwin()

def update_values(screen, top, val):
    left_pos = 20
    screen.addstr(top, left_pos, str(val)+ "            ")
    screen.refresh()


