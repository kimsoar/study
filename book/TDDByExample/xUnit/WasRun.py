from TestCase import TestCase


class WasRun(TestCase):
    def testMethod(self):
        self.wasRun = None
        self.wasRun = 1

    def setUp(self):
        self.wasSetUp = 1
        
    
        