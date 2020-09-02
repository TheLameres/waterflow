package thelameres.waterflow.core;

import io.undertow.Undertow;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;

public class WaterflowApplication {

  Mode mode;

  public WaterflowApplication(Mode mode, Package aPackage) {
    this.mode = mode;
    Properties.aPackage = aPackage;
  }

  public void startApplication() {
    SeContainerInitializer initializer = SeContainerInitializer.newInstance();
    initializer.addPackages(true, Properties.aPackage);
    SeContainer container = initializer.initialize();
    UndertowJaxrsServer server = new UndertowJaxrsServer();
    ResteasyDeployment deployment = new ResteasyDeployment();
    deployment.setApplicationClass(Application.class.getName());
    deployment.setInjectorFactoryClass("org.jboss.resteasy.cdi.CdiInjectorFactory");
    DeploymentInfo deploymentInfo = server.undertowDeployment(deployment, "/");
    deploymentInfo.setClassLoader(WaterflowApplication.class.getClassLoader());
    deploymentInfo.setContextPath("/api");
    deploymentInfo.setDeploymentName("Undertow + Resteasy example");
    deploymentInfo.addListener(
        Servlets.listener(org.jboss.weld.environment.servlet.Listener.class));
    server.deploy(deploymentInfo);
    Undertow.Builder builder = Undertow.builder()
            .addHttpListener(8080, "localhost");

    server.start(builder);
  }

  public static void run(Class<?> type, Mode mode) {
    Reflections reflections = new Reflections(Properties.aPackage.getName(),
            new SubTypesScanner(false),
            new TypeAnnotationsScanner()
    );
    Properties.resourceClass = reflections.getTypesAnnotatedWith(javax.ws.rs.Path.class);
    WaterflowApplication instance = new WaterflowApplication(mode, type.getPackage());
    instance.startApplication();
  }

  public static void run(Class<?> type) {
    run(type, Mode.DEVELOPMENT);
  }
}
