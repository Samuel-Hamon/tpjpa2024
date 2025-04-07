

import java.util.HashSet;
import java.util.Set;

import rest.ArtisteResource;
import rest.CORSFilter;
import rest.ConcertResource;
import rest.GenreMusicalResource;
import rest.OrganisateurResource;
import rest.TicketResource;
import rest.UtilisateurResource;
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
        clazzes.add(TicketResource.class);
        clazzes.add(OrganisateurResource.class);
        clazzes.add(UtilisateurResource.class);
        clazzes.add(CORSFilter.class);
//        clazzes.add(AcceptHeaderOpenApiResource.class);
         

        return clazzes;
    }

}