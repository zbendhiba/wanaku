package ai.wanaku.server.quarkus.api.v1.tools;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import ai.wanaku.api.exceptions.WanakuException;
import ai.wanaku.api.types.ToolReference;
import ai.wanaku.api.types.WanakuResponse;
import ai.wanaku.core.util.CollectionsHelper;
import ai.wanaku.server.quarkus.api.v1.forwards.ForwardsBean;
import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

@ApplicationScoped
@Path("/api/v1/tools")
public class ToolsResource {
    private static final Logger LOG = Logger.getLogger(ToolsResource.class);

    @Inject
    ToolsBean toolsBean;

    @Inject
    ForwardsBean forwardsBean;

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(ToolReference resource) throws WanakuException {
        toolsBean.add(resource);
        return Response.ok().build();
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<WanakuResponse<List<ToolReference>>> list() throws WanakuException {
        List<ToolReference> forwardTools = forwardsBean.listAllAsTools();
        List<ToolReference> tools = toolsBean.list();

        List<ToolReference> ret = CollectionsHelper.join(tools, forwardTools);

        return RestResponse.ok(new WanakuResponse<>(ret));
    }

    @Path("/remove")
    @PUT
    public Response remove(@QueryParam("tool") String tool) throws WanakuException {
        toolsBean.remove(tool);
        return Response.ok().build();
    }
}
