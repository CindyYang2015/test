# coding=utf-8
import socket
class mytool():
    def __init__(self):
        pass
    def send(self,ipaddress,port,sendData):
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        serverAddr = (ipaddress,int(port))
        sock.connect(serverAddr)
        print('connect success')
        size=len(sendData)
        sendData="%08d%s" %(size,sendData)
        if len(sendData)>0:
            sock.send(sendData)
        recvData = sock.recv(1024)

        print('the receive message is:%s'%recvData).decode('utf-8')
        sock.close()
        print('close socket!')
        return recvData.decode('utf-8')
if __name__ == '__main__':
    pass



