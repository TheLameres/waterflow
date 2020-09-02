package thelameres.waterflow.core;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;

public class Application extends javax.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        return Properties.resourceClass;
    }
}
