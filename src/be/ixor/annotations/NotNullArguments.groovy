package be.ixor.annotations

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import java.lang.annotation.Target
import java.lang.annotation.ElementType

/**
 * Annotated methods will assert that all parameters, or only specific if specified, are not null.
 * <p/>
 * Usage:
 * <li>@NotNullArguments : will add assert not null for all parameters</li>
 * <li>@NotNullArgumnts(["arg1","arg2"]) : will add assert not null for
 * the parameters with name arg1 and arg2.</li>
 *
 * @author rverlind
 * @created May 19, 2009
 */

@Retention(RetentionPolicy.SOURCE)
@Target([ElementType.METHOD])
@GroovyASTTransformationClass(["be.ixor.annotations.AssertNotNullArgumentsASTTransformation"])
public @interface NotNullArguments {
      String[] value();
}