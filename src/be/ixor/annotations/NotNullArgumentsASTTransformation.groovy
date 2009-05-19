package be.ixor.annotations

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.BooleanExpression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.AssertStatement
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.objectweb.asm.Opcodes
import org.codehaus.groovy.ast.AnnotationNode

/**
 * Adds a not null assertion for all method parameters if no value was provided
 * for the annotation, or only for the specified parameters if a list of
 * parameter names was specified for the annotation. 
 *
 * @author rverlind
 * @created May 19, 2009
 */
@GroovyASTTransformation (phase = CompilePhase.SEMANTIC_ANALYSIS)
public class AssertNotNullArgumentsASTTransformation implements ASTTransformation, Opcodes {

    public void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        assert astNodes[1] instanceof MethodNode

        AnnotationNode annotation = astNodes[0]
        List parameterNames = getParameterNames(annotation.members.values())
        MethodNode method = astNodes[1]

        Statement code = method.getCode()
        BlockStatement blockStatement = new BlockStatement()

        Parameter[] parameters = method.getParameters()
        parameters.each {parameter ->
            if(parameterNames.isEmpty() || parameterNames.contains(parameter.name)){
                blockStatement.addStatement(createAssertStatement(parameter))
            }
        }

        blockStatement.addStatement(code)
        method.setCode(blockStatement)
    }

    public Statement createAssertStatement(Parameter parameter) {
        return new AssertStatement(
                new BooleanExpression(
                        new VariableExpression(parameter)))
    }


    private String[] getParameterNames(Collection parameterNames){
        parameterNames.collectAll( { it -> it.expressions.collect { lit -> lit.value}}).flatten()
    }
}