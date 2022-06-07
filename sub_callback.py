import paho.mqtt.subscribe as subscribe

def on_message(client, userdata, message):
	print(str(message.payload.decode("utf-8")))

subscribe.callback(on_message, "test/status", hostname="10.42.0.66")