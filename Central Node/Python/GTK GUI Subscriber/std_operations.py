import sys
import select

def _get_char():
    #If there is an input read it
    while sys.stdin in select.select([sys.stdin], [], [], 0)[0]:
    #for line in sys.stdin:
        line = sys.stdin.read(1)
        if (line):
            #Return read char
            return str(line)
        else: #stdin has been closed
            print ("eof")
            return 'a'
