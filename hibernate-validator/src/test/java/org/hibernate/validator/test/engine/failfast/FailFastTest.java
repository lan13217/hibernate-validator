package org.hibernate.validator.test.engine.failfast;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

import org.testng.annotations.Test;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.HibernateValidatorFactory;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.test.util.TestUtil;

import static org.hibernate.validator.test.util.TestUtil.assertNumberOfViolations;

/**
 * @author Emmanuel Bernard
 * @author Kevin Pollet - SERLI - kevin.pollet@serli.com
 */
public class FailFastTest {
	@Test
	public void testFailFastDefaultBehaviour() {
		final HibernateValidatorConfiguration configuration = TestUtil.getConfiguration( HibernateValidator.class );
		final ValidatorFactory factory = configuration.buildValidatorFactory();

		final Validator validator = factory.getValidator();
		A testInstance = new A();

		Set<ConstraintViolation<A>> constraintViolations = validator.validate( testInstance );
		assertNumberOfViolations( constraintViolations, 2 );
	}

	@Test
	public void testFailFastSetOnValidatorFactory() {
		final HibernateValidatorConfiguration configuration = TestUtil.getConfiguration( HibernateValidator.class );
		final ValidatorFactory factory = configuration.failFast( true ).buildValidatorFactory();

		final Validator validator = factory.getValidator();
		A testInstance = new A();

		Set<ConstraintViolation<A>> constraintViolations = validator.validate( testInstance );
		assertNumberOfViolations( constraintViolations, 1 );
	}

	@Test
	public void testFailFastSetOnValidator() {
		final HibernateValidatorConfiguration configuration = TestUtil.getConfiguration( HibernateValidator.class );
		final ValidatorFactory factory = configuration.buildValidatorFactory();

		final Validator validator =
				factory.unwrap( HibernateValidatorFactory.class )
						.usingHibernateContext()
						.failFast( true )
						.getValidator();
		A testInstance = new A();

		Set<ConstraintViolation<A>> constraintViolations = validator.validate( testInstance );
		assertNumberOfViolations( constraintViolations, 1 );
	}

	@Test
	public void testFailFastSetWithProperty() {
		final HibernateValidatorConfiguration configuration = TestUtil.getConfiguration( HibernateValidator.class );
		final ValidatorFactory factory = configuration.addProperty( HibernateValidatorConfiguration.FAIL_FAST, "true" )
				.buildValidatorFactory();

		final Validator validator = factory.getValidator();
		A testInstance = new A();

		Set<ConstraintViolation<A>> constraintViolations = validator.validate( testInstance );
		assertNumberOfViolations( constraintViolations, 1 );
	}

	@Test
	public void testFailFastSetWithInvalidProperty() {
		final HibernateValidatorConfiguration configuration = TestUtil.getConfiguration( HibernateValidator.class );

		//Default fail fast property value is false
		final ValidatorFactory factory = configuration.addProperty(
				HibernateValidatorConfiguration.FAIL_FAST, "not correct"
		).buildValidatorFactory();

		final Validator validator = factory.getValidator();
		A testInstance = new A();

		Set<ConstraintViolation<A>> constraintViolations = validator.validate( testInstance );
		assertNumberOfViolations( constraintViolations, 2 );
	}

	class A {
		@NotNull
		String b;

		@NotNull
		@Email
		String c;
	}
}