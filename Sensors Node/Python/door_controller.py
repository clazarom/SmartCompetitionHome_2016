import RPi.GPIO as GPIO

#Set pins mode - BCM
GPIO.setmode(GPIO.BCM)

#Pins number:
door_pin = 22
inc1_pin = 27
inc2_pin = 17
#Set pins
GPIO.setup(door_pin, GPIO.IN)
GPIO.setup(inc1_pin, GPIO.IN)
#GPIO.setup(inc2_pin, GPIO.IN)

#Function to read the door state
def check_door():
	#Read input value
	input_state = GPIO.input(door_pin)
	return input_state

#Function to read any of the two other incidents
def check_inc(input):
	#Check either pin
	input_state = 0
	if input == 1:
		input_state = GPIO.input(inc1_pin)
	elif input == 2:
		input_state = GPIO.input(inc1_pin)
	else:
		input_state = -1
	#Return the result
	return input_state
			
