package com.thotsoft.carpooling.services.rest;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;

import java.io.File;

import static org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencies.createDependency;

public class CreateDeployment {

    public static WebArchive createDeployment() {
        JavaArchive contentModelJar = ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "com.thotsoft.carpooling")
                .addAsResource("META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return deployment().addAsLibrary(contentModelJar);
    }

    private static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class, "carpooling.war")
                .addAsLibraries(mavenDependencies())
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF", "jboss-web.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF", "web.xml"))
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF", "keycloak.json"));
    }

    private static File[] mavenDependencies() {
        return Maven.configureResolver().withMavenCentralRepo(false)
                .loadPomFromFile("pom.xml").importCompileAndRuntimeDependencies()
                .addDependencies(
                        createDependency("org.jboss.shrinkwrap.resolver:shrinkwrap-resolver-impl-maven",
                                ScopeType.TEST, false)
                )
                .resolve().withTransitivity().asFile();
    }
}
