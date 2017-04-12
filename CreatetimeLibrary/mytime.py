import time
class mytooltime():
    def __init__(self):
        pass
    def timestamp(self,timeformat='%Y%m%d%H%M%S'):
        return time.strftime(timeformat, time.localtime(time.time()))
    print(timestamp('%Y%m%d%H%M%S'))
if __name__ == '__main__':
    pass
