package iit.ece.edu.smartcompetitionhouse;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import org.eclipse.paho.client.mqttv3.MqttException;


public class SmartHomeActivity extends AppCompatActivity {

    private Button sendMessage;
    private Button receiveMessage;
    private Button notReceiveMessage;

    private EditText messageToPublish;

    public static EditText doorState;
    public static EditText tempState;
    public static EditText pressState;
    public static EditText altitudeState;
    public static EditText warningState;

    private static MyCustomMqttClient sampleClient;
    private static SampleAsyncCallBack sampleSubscriber;

    //Default Client:
    // Default settings:
    private static boolean quietMode 	= false;
    private static String action 		= "publish";
    private static String action_async = "subscribe";
    //String topic 		= "hello/world";
    private static String message 		= "Message from blocking Paho MQTTv3 Java client sample";
    private static int qos 			= 2;
    //TODO Set the address for hotspot
    private static String broker 		= "192.168.0.16";
    private static int port 			= 1883;
    private static String clientId 	= null;
    //TODO May be subscribe to each topic separetly ???
    private static String subTopic		= "sensors/#";
    private static String pubTopic 	= "sensors/app";
    private static boolean cleanSession = true;			// Non durable subscriptions
    private boolean ssl = false;
    private static String password = null;
    private static String userName = null;
    public static boolean subscribed = true;

    private static String protocol = "tcp://";

    public static String readTemp = "";
    public static String readPress ="";
    public static String readDoor = "";
    public static String readWarning = "";
    public static String readAlt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Set Buttons
        sendMessage = (Button) findViewById(R.id.mqtt_publish);
        sendMessage.setOnClickListener(publishClick);
        receiveMessage = (Button) findViewById(R.id.mqtt_subscribe);
        receiveMessage.setOnClickListener(subscribeClick);
        notReceiveMessage = (Button) findViewById(R.id.mqtt_unsubscribe);
        notReceiveMessage.setOnClickListener(unsubscribeClick);

        //Get text edit fields
        doorState = (EditText)findViewById(R.id.door_state);
        tempState = (EditText)findViewById(R.id.temperature_state);
        pressState = (EditText)findViewById(R.id.pressure_state);
        warningState = (EditText)findViewById(R.id.warning_state);
        messageToPublish = (EditText)findViewById(R.id.message_publish);
        altitudeState = (EditText)findViewById(R.id.altitude_state);

        tempState.setText("0 C");


        //Create default Paho Client
        //AsyncrhonousReadSubscribe aRead = new AsyncrhonousReadSubscribe();
        //aRead.execute();
        createMQTTDefaultClients();
    }

    /*
	 * On click listener
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */

    private OnClickListener publishClick =  new OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Publish on the queue
            int QoS = 1;
            String content = "Content super interesting";
            content = messageToPublish.getText().toString();
            byte[] payload = content.getBytes();

            try{
                sampleClient.publish(pubTopic, QoS, payload);
            }catch (MqttException e){
                System.out.println("MQTT Exception in publish: "+e);

            }

        }};

    private OnClickListener subscribeClick =  new OnClickListener(){

        @Override
        public void onClick(View v) {
            updateFields();
            subscribed = true;
            checkSubscription();
            //createMQTTDefaultClients();


        }};

    private OnClickListener unsubscribeClick =  new OnClickListener(){

        @Override
        public void onClick(View v) {
            subscribed = false;
            sampleSubscriber.unsubscribe();


        }};




    public  static void createMQTTDefaultClients(){


        String url = protocol + broker + ":" + port;
        clientId = "secure_phone_"+action;

        // With a valid set of arguments, the real work of
        // driving the client API can begin
        try {
            // Create an instance of this class
            sampleClient = new MyCustomMqttClient(url, clientId, cleanSession, quietMode,userName,password);

            // Perform the requested action
            sampleClient.publish(pubTopic,qos,message.getBytes());

            //For the async
            // Create an instance of the Sample client wrapper
            clientId = "secure_phone_"+action_async;
            sampleSubscriber = new SampleAsyncCallBack(url,clientId,cleanSession, quietMode,userName,password);
            sampleSubscriber.subscribe(subTopic,qos);


        } catch(Throwable me) {
            // Display full details of any exception that occurs
            System.out.println("reason "+((MqttException) me).getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }


    }

    public void checkSubscription(){

        try{
            if (sampleSubscriber ==null){
                System.out.println("No subscriber");
                String url = protocol + broker + ":" + port;
                clientId = "SmartPhone_"+action_async;
                sampleSubscriber = new SampleAsyncCallBack(url,clientId,cleanSession, quietMode,userName,password);

            }
            System.out.println("Subscriber - subscribe");
            // Create an instance of the Sample client wrapper
            //String url = protocol + broker + ":" + port;
            //clientId = "SmartPhone_"+action_async;
            //sampleSubscriber = new SampleAsyncCallBack(url,clientId,cleanSession, quietMode,userName,password);

            sampleSubscriber.state = SampleAsyncCallBack.BEGIN;
            sampleSubscriber.subscribe(subTopic,qos);



        }catch(Throwable me) {
            // Display full details of any exception that occurs
            System.out.println("reason "+((MqttException) me).getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }


    }

    public static void updateFields(){
        pressState.setText(readPress);
        tempState.setText(readTemp);
        doorState.setText(readDoor);
        warningState.setText(readWarning);
        altitudeState.setText(readAlt);


    }

    static void printHelp() {
        System.out.println(
                "Syntax:\n\n" +
                        "    Sample [-h] [-a publish|subscribe] [-t <topic>] [-m <message text>]\n" +
                        "            [-s 0|1|2] -b <hostname|IP address>] [-p <brokerport>] [-i <clientID>]\n\n" +
                        "    -h  Print this help text and quit\n" +
                        "    -q  Quiet mode (default is false)\n" +
                        "    -a  Perform the relevant action (default is publish)\n" +
                        "    -t  Publish/subscribe to <topic> instead of the default\n" +
                        "            (publish: \"Sample/Java/v3\", subscribe: \"Sample/#\")\n" +
                        "    -m  Use <message text> instead of the default\n" +
                        "            (\"Message from MQTTv3 Java client\")\n" +
                        "    -s  Use this QoS instead of the default (2)\n" +
                        "    -b  Use this name/IP address instead of the default (m2m.eclipse.org)\n" +
                        "    -p  Use this port instead of the default (1883)\n\n" +
                        "    -i  Use this client ID instead of SampleJavaV3_<action>\n" +
                        "    -c  Connect to the server with a clean session (default is false)\n" +
                        "     \n\n Security Options \n" +
                        "     -u Username \n" +
                        "     -z Password \n" +
                        "     \n\n SSL Options \n" +
                        "    -v  SSL enabled; true - (default is false) " +
                        "    -k  Use this JKS format key store to verify the client\n" +
                        "    -w  Passpharse to verify certificates in the keys store\n" +
                        "    -r  Use this JKS format keystore to verify the server\n" +
                        " If javax.net.ssl properties have been set only the -v flag needs to be set\n" +
                        "Delimit strings containing spaces with \"\"\n\n" +
                        "Publishers transmit a single message then disconnect from the server.\n" +
                        "Subscribers remain connected to the server and receive appropriate\n" +
                        "messages until <enter> is pressed.\n\n"
        );
    }


}
