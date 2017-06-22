#!/usr/bin/python3
#!/usr/bin/env python
# coding: utf-8

import socket

socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
socket.bind(("", 5000))

while True:
        socket.listen(5)
        client, address = socket.accept()
        print("{} connected".format(address))

        response = client.recv(255)
        if response == "ok":
                print(response)
        elif response == "Connection: close":
                print("Close")
                client.close()
stocket.close()