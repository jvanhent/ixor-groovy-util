package be.ixor.annotations
public class NotNullReturnTest extends GroovyTestCase {

  public void testWithGoodResult() {
    NotNullReturnTestObject testObject = new NotNullReturnTestObject()
    assert 'something' == testObject.returnSomething()
    assert 1 == testObject.counter
  }

  public void testWithException() {
    try {
      new NotNullReturnTestObject().throwException()
      fail()
    } catch (Throwable e) {
      // ok
    }
  }

  public void testNullReturned() {
    NotNullReturnTestObject testObject = new NotNullReturnTestObject()
    try {
      testObject.returnNull()
      fail()
    } catch (AssertionError e) {
      // ok
    }
    assert 1 == testObject.counter
  }
}