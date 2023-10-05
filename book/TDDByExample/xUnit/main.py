from TestCaseTest import TestCaseTest
from TestResult import TestResult


if __name__ == "__main__":
    result = TestResult()
    TestCaseTest("testTemplateMethod").run(result)
    TestCaseTest("testResult").run(result)
    TestCaseTest("testFailedResult").run(result)
    TestCaseTest("testFailedResultFormatting").run(result)
    TestCaseTest("testSuite").run(result)
    print result.summary()