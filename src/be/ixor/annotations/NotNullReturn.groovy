package be.ixor.annotations

import org.codehaus.groovy.transform.GroovyASTTransformationClass
import java.lang.annotation.*
import java.lang.annotation.Target

/**
 * Annotated methods will assert that the return value of the method is not <code>null</code>.
 * @author jvanhent
 */

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass(["be.ixor.annotations.NotNullReturnASTTransformation"])
public @interface NotNullReturn {

}