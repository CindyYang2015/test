# coding=utf-8
import datetime
import time
import random
class mysuiji():
    def __init__(self):
        pass
    def stime(self,stime='%Y%m%d%H%M%S'):
        return time.strftime(stime, time.localtime(time.time()))
    print(stime('%Y%m%d%H%M%S'))

    def createnumber(self):
        nowTime=datetime.datetime.now().strftime("%Y%m%d%H%M%S")
        randomNum=random.randint(0,100)
        if randomNum<=10:
            randomNum=str(0)+str(randomNum)
        uniqueNum=str(nowTime)+str(randomNum)
        return uniqueNum

if __name__ == '__main__':
    pass



