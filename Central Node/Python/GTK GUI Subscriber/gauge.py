
import plotly.plotly as py
import plotly.graph_objs as go
from plotly.offline import download_plotlyjs, init_notebook_mode, plot, iplot

base_chart = {
    "values": [18, 16, 17, 17, 17, 15, 10],
    "labels": ["-", "-10", "0", "10", "20", "30", "40"],
    "domain": {"x": [0, .48]},
    "marker": {
	"colors": [
	    'rgb(255, 255, 255)',
	    'rgb(255, 255, 255)',
	    'rgb(255, 255, 255)',
	    'rgb(255, 255, 255)',
	    'rgb(255, 255, 255)',
	    'rgb(255, 255, 255)',
	    'rgb(255, 255, 255)'
	],
	"line": {
	    "width": 1
	}
    },
    "name": "Gauge",
    "hole": .4,
    "type": "pie",
    "direction": "clockwise",
    "rotation": 145,
    "showlegend": False,
    "hoverinfo": "none",
    "textinfo": "label",
    "textposition": "outside",
    
}



meter_chart = {
    "values": [ 30, 15, 15, 15, 15, 10],
    "labels": ["Room temperature", "C", "MC", "M", "MH", "H"],
    "marker": {
	'colors': [
	    'rgb(0, 0, 0)',
	    'rgb(8,117,255)',
	    'rgb(8,242,133)',
	    'rgb(63,242,8)',
	    'rgb(200,187,8)',
	    'rgb(255,0,0)'
	    
	]
    },
    "domain": {"x": [0, 0.48]},
    "name": "Temp",
    "hole": .3,
    "type": "pie",
    "direction": "clockwise",
    "rotation": 125,
    "showlegend": False,
    "textinfo": "label",
    "textposition": "inside",
    "hoverinfo": "none",
    
}

layout2 = go.Layout(
    autosize=False,
    width=200,
    height=220,
    margin=go.Margin(
        l=60,
        r=0,
        b=0,
        t=0,
        pad=4
    ),
    paper_bgcolor='rgba(0,0,0,0)',
    plot_bgcolor='rgba(0,0,0,0)'
)

def generate_temp_fig(temp):
	layout = generate_temp_layout(temp)
	
	# we don't want the boundary now
	base_chart['marker']['line']['width'] = 0

	fig = {"data": [base_chart, meter_chart],
	       "layout": layout}

	py.image.save_as(fig, 'temp_plot1.jpeg')

	#init_notebook_mode()
	#plot(fig, filename='temp-meter-chart')
	return fig
	

def generate_temp_layout(temp):

	path_in = obtain_pick(temp)

	layout = {
	     'autosize':False,
    	     'width':400,
    	     'height': 300 ,
    	     'margin': {
        	'l': 60,
        	'r': 0,
        	'b': 0,
        	't': 0,
        	'pad': 4
    	     },
    	    'paper_bgcolor':'rgb(0,0,0)',
    	    'plot_bgcolor':'rgb(0,0,0)',

	    
	    'xaxis': {
		'showticklabels': False,
		'autotick': False,
		'showgrid': False,
		'zeroline': False,
	    },
	    'yaxis': {
		'showticklabels': False,
		'autotick': False,
		'showgrid': False,
		'zeroline': False,
	    },
	    'shapes': [
		{
		    'type': 'path',
		    #'path': 'M 0.235 0.5 L 0.24 0.65 L 0.245 0.5 Z',
		    'path': path_in,
		    'fillcolor': 'rgba(44, 160, 101, 0.5)',
		    'line': {
		        'width': 0.5
		    },
		    'xref': 'paper',
		    'yref': 'paper',
		    
		}
	    ],
	    'annotations': [
		{
		    'xref': 'paper',
		    'yref': 'paper',
		    'x': 0.23,
		    'y': 0.45,
		    'text': temp,
		    'showarrow': False
		}
	    ]
	}
	return layout

h = 0.15
b = 0.01

r = 0.015
x_cir = 0.23
y_cir = 0.45

def obtain_pick(temp):

	#Values from -10 to 0
	if temp < -5:
		x_init = 0.18
		y_init = 0.44
		x1 = 0.235
		y1 = 0.5
		x2 = 0.235
		y2 = 0.5

	#Values from -5 - 0
	elif temp < 0:
		x_init = 0.18
		y_init = 0.48
		x1 = 0.235
		y1 = 0.5

		x2 = 0.235
		y2 = 0.5

	#Values from 0 - 5
	elif temp < 5:
		x_init = 0.19
		y_init = 0.6

		x1 = 0.235
		y1 = 0.51

		x2 = 0.235
		y2 = 0.49
	#Values from 5 - 10
	elif temp < 10:
		x_init = 0.22
		y_init = 0.64
		
		x1 = 0.235
		y1 = 0.51

		x2 = 0.235
		y2 = 0.49
	#Values from 15 - 20
	elif temp < 15:
		x_init = 0.24
		y_init = 0.65
		
		x1 = 0.235
		y1 = 0.51

		x2 = 0.235
		y2 = 0.49
	#Values from 20-25
	elif temp < 20:
		x_init = 0.26
		y_init = 0.65

		x1 = 0.235
		y1 = 0.51

		x2 = 0.237
		y2 = 0.49
	#Values from 25-30
	elif temp < 25:
		x_init = 0.28
		y_init = 0.65
		
		x1 = 0.235
		y1 = 0.51

		x2 = 0.237
		y2 = 0.49
	elif temp < 30:
		x_init = 0.29
		y_init = 0.55
		
		x1 = 0.235
		y1 = 0.51

		x2 = 0.237
		y2 = 0.49
	elif temp < 35:
		x_init = 0.28
		y_init = 0.48
		
		x1 = 0.234
		y1 = 0.51

		x2 = 0.237
		y2 = 0.49
	#Values from 30-40
	elif temp >35:
		x_init = 0.28
		y_init = 0.42

		x1 = 0.234
		y1 = 0.51

		x2 = 0.236
		y2 = 0.49



	ret_path = 'M '+str(x_init)+' '+str(y_init) +' L '+str(x1)+' '+str(y1)+ ' L '+str(x2)+ ' '+str(y2)+' Z'
	
	return ret_path




