package be.ixor.annotations

import be.ixor.annotations.NotNullArguments

/**
 * NotNullArgumentTest  
 *
 * @author rverlind
 * @created May 19, 2009
 */

public class NotNullArgumentTest extends GroovyTestCase {

    public void testDefaultAnnotation() {
        new NotNullArgumentTestObject().allArgumentsNotNull("arg1", "arg2")

        try {
            new NotNullArgumentTestObject().allArgumentsNotNull("arg1", null)
        } catch (java.lang.AssertionError ae) {
            // As expected
        }
    }

    public void testAnnotationWithParameterNames() {
        new NotNullArgumentTestObject().secondArgumentNotNull(null, "arg2")

        try {
            new NotNullArgumentTestObject().secondArgumentNotNull("arg1", null)
        } catch (java.lang.AssertionError ae) {
            // As expected
        }
    }

    public void testRegularNonAnnotatedMethod(){
       new NotNullArgumentTestObject().noArgumentsNotNull(null,null)  
    }



}


class NotNullArgumentTestObject {

    @NotNullArguments
    public void allArgumentsNotNull(Object arg1, Object arg2) {
        // Do nothing
    }

    @NotNullArguments (["arg2"])
    public void secondArgumentNotNull(Object arg1, Object arg2) {
        // Do nothing
    }

    public void noArgumentsNotNull(Object arg1, Object arg2) {
        // Do nothing
    }

}