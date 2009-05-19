package be.ixor.annotations

import org.codehaus.groovy.transform.GroovyASTTransformation
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.PropertyNode
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codehaus.groovy.ast.expr.PropertyExpression
import org.codehaus.groovy.ast.expr.FieldExpression
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.expr.BinaryExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.ArgumentListExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression
import java.lang.reflect.Modifier
import org.codehaus.groovy.classgen.Verifier
import org.codehaus.groovy.ast.ClassHelper
import org.objectweb.asm.Opcodes
import org.codehaus.groovy.ast.stmt.IfStatement
import org.codehaus.groovy.ast.expr.BooleanExpression
import org.codehaus.groovy.ast.stmt.ThrowStatement
import org.codehaus.groovy.ast.expr.ConstructorCallExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.SynchronizedStatement
import org.codehaus.groovy.ast.expr.ClassExpression
import org.codehaus.groovy.syntax.Token
import org.codehaus.groovy.ast.stmt.AssertStatement
import org.codehaus.groovy.ast.expr.DeclarationExpression
import org.codehaus.groovy.syntax.Types

@GroovyASTTransformation (phase = CompilePhase.SEMANTIC_ANALYSIS)
public class NotNullReturnASTTransformation implements ASTTransformation, Opcodes {


  public NotNullReturnASTTransformation() {

  }

  public void visit(ASTNode[] nodes, SourceUnit source) {
    AnnotatedNode parent = (AnnotatedNode) nodes[1];
    AnnotationNode node = (AnnotationNode) nodes[0];

    if (!(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
      throw new RuntimeException("Internal error: wrong types: $node / $parent");
    }


    if (parent instanceof MethodNode) {
      MethodNode methodNode = (MethodNode) parent;
      final ClassNode type = methodNode.getReturnType();
      final Map fieldMethods = type.getDeclaredMethodsMap();
      final ClassNode owner = methodNode.getDeclaringClass();

      final Map ownMethods = owner.getDeclaredMethodsMap();
      def notNullMethodNode = addNotNullMethod(methodNode, owner, ownMethods);
      redirectCurrentMethod(methodNode, notNullMethodNode)
    }
  }

  def addNotNullMethod(MethodNode method, ClassNode owner, Map ownMethods) {
    if (method.isStatic())
      return;

    def notNullMethodName = method.getName() + '_NotNullCheck'
    if (!ownMethods.containsKey(notNullMethodName)) {
      owner.addMethod(notNullMethodName,
              method.getModifiers() & -ACC_ABSTRACT,
              method.getReturnType(),
              method.getParameters(),
              method.getExceptions(),
              method.getCode());
    }
  }

  def redirectCurrentMethod(MethodNode method, MethodNode notNullMethodNode) {
    MethodCallExpression methodCall = new MethodCallExpression(
            VariableExpression.THIS_EXPRESSION,
            notNullMethodNode.getName(),
            new ArgumentListExpression(method.getParameters()))

    BlockStatement block = new BlockStatement();
    VariableExpression resultVariable = new VariableExpression("result", method.getReturnType());
    block.addStatement(new ExpressionStatement(
            new DeclarationExpression(resultVariable,
                    Token.newSymbol(Types.EQUALS, 0, 0),
                    methodCall)));
    block.addStatement(new AssertStatement(new BooleanExpression(new BinaryExpression(resultVariable, Token.newSymbol(Types.COMPARE_NOT_EQUAL, 0, 0), ConstantExpression.NULL))))
    block.addStatement(new ReturnStatement(new ExpressionStatement(resultVariable)))
    method.code = block
  }
}