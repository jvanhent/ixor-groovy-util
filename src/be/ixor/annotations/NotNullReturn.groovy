package be.ixor.annotations

import org.codehaus.groovy.transform.GroovyASTTransformationClass
import java.lang.annotation.*
import java.lang.annotation.Target

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass(["be.ixor.annotations.NotNullReturnASTTransformation"])
public @interface NotNullReturn {

}