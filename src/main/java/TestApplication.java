

import java.util.HashSet;
import java.util.Set;

import rest.ArtisteResource;
import rest.ConcertResource;
import rest.GenreMusicalResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/")
public class TestApplication extends Application {
	

    @Override
    public Set<Class<?>> getClasses() {

        final Set<Class<?>> clazzes = new HashSet<Class<?>>();

        clazzes.add(OpenApiResource.class);
        clazzes.add(ConcertResource.class);
        clazzes.add(ArtisteResource.class);
        clazzes.add(GenreMusicalResource.class);
//        clazzes.add(AcceptHeaderOpenApiResource.class);
         

        return clazzes;
    }

}