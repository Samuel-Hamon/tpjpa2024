

import java.util.HashSet;
import java.util.Set;

import rest.ArtisteResource;
import rest.CORSFilter;
import rest.ConcertResource;
import rest.GenreMusicalResource;
import rest.OrganisateurResource;
import rest.SwaggerResource;
import rest.TicketResource;
import rest.UtilisateurResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@OpenAPIDefinition(
	    info = @Info(
	        title = "API de gestion de concerts",
	        version = "1.0",
	        description = "Documentation de l'API REST pour gérer les artistes, concerts, tickets, etc.",
	        contact = @Contact(name = "Hamon Samuel - Amoussou Guenou Magnolia", email = "samuel.hamon.miage@gmail.com"),
	        license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
	    ),
	    servers = {
	        @Server(url = "http://localhost:8080", description = "Serveur local")
	    }
	)
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
        clazzes.add(SwaggerResource.class);
//        clazzes.add(AcceptHeaderOpenApiResource.class);
         

        return clazzes;
    }

}