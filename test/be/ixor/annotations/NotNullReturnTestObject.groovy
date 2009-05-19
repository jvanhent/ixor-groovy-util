package be.ixor.annotations

import be.ixor.annotations.NotNullReturn

class NotNullReturnTestObject {

  def counter = 0
  
  @NotNullReturn
  String throwException() {
    throw new Throwable()
  }

  @NotNullReturn
  String returnSomething() {
    counter++
    return 'something'
  }

  @NotNullReturn
  String returnNull() {
    counter++
    return null
  }
}