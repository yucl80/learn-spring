package yucl.learn.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LearnSpringApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test public void expressionPropertiesAreResolvedAgainsAppContextBeans() throws Exception {
		DefaultWebSecurityExpressionHandler handler=new DefaultWebSecurityExpressionHandler();
		StaticApplicationContext appContext=new StaticApplicationContext();
		RootBeanDefinition bean=new RootBeanDefinition(SecurityConfig.class);
		bean.getConstructorArgumentValues().addGenericArgumentValue("ROLE_A");
		appContext.registerBeanDefinition("role",bean);
		handler.setApplicationContext(appContext);
		EvaluationContext ctx=handler.createEvaluationContext(mock(Authentication.class),mock(FilterInvocation.class));
		ExpressionParser parser=handler.getExpressionParser();
		assertTrue(parser.parseExpression("@role.getAttribute() == 'ROLE_A'").getValue(ctx,Boolean.class));
		assertTrue(parser.parseExpression("@role.attribute == 'ROLE_A'").getValue(ctx,Boolean.class));
	}

}
