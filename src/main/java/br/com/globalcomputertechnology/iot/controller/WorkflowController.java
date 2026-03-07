package br.com.globalcomputertechnology.iot.controller;

import br.com.globalcomputertechnology.iot.service.WorkflowService;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/workflow")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @POST
    @Path("/start")
    public Response startWorkflow() {

        workflowService.startProcess();

        return Response.ok("Process started").build();
    }
}