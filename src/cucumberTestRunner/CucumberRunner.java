package cucumberTestRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import gherkin.formatter.Formatter;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.junit.Assertions;
import cucumber.runtime.junit.JUnitOptions;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberFeature;


public class CucumberRunner extends ParentRunner<cucumber.runtime.junit.FeatureRunner> {
	 private final List<cucumber.runtime.junit.FeatureRunner> children = new ArrayList<cucumber.runtime.junit.FeatureRunner>();
	 private final JUnitReporter jUnitReporter;
	 private final Runtime runtime;
	 private final Formatter formatter;

	public CucumberRunner(Class<?> clazz) throws InitializationError, IOException {
		super(clazz);

        ClassLoader classLoader = clazz.getClassLoader();
        Assertions.assertNoCucumberAnnotatedMethods(clazz);

        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();

        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        runtime = createRuntime(resourceLoader, classLoader, runtimeOptions);
        formatter = runtimeOptions.formatter(classLoader);
        final JUnitOptions junitOptions = new JUnitOptions(runtimeOptions.getJunitOptions());
        
        jUnitReporter = new JUnitReporter(runtimeOptions.reporter(classLoader), formatter, runtimeOptions.isStrict(), junitOptions);
        addChildren(runtimeOptions.cucumberFeatures(resourceLoader));
	        
	}

	protected void runChild(cucumber.runtime.junit.FeatureRunner child, RunNotifier notifier) {
        child.run(notifier);
    }
	 
	private void addChildren(List<CucumberFeature> cucumberFeatures) throws InitializationError {
		for (CucumberFeature cucumberFeature : cucumberFeatures) {
			children.add(new FeaturesRunner(cucumberFeature, runtime, jUnitReporter));
		}
	}

    /**
     * Create the Runtime. Can be overridden to customize the runtime or backend.
     *
     * @param resourceLoader used to load resources
     * @param classLoader    used to load classes
     * @param runtimeOptions configuration
     * @return a new runtime
     * @throws InitializationError if a JUnit error occurred
     * @throws IOException if a class or resource could not be loaded
     */
    protected Runtime createRuntime(ResourceLoader resourceLoader, ClassLoader classLoader,
                                    RuntimeOptions runtimeOptions) throws InitializationError, IOException {
        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        return new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
    }

    @Override
    public List<cucumber.runtime.junit.FeatureRunner> getChildren() {
        return children;
    }

    @Override
    protected Description describeChild(cucumber.runtime.junit.FeatureRunner child) {
        return child.getDescription();
    }


    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
        jUnitReporter.done();
        jUnitReporter.close();
        runtime.printSummary();
    }
}
