package com.globant.jira.api;

import com.globant.jira.models.ExecutionResults;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Take a look at http://confluence.xpand-addons.com/display/XRAY/Import+Execution+Results+-+REST
 * <p>
 * to learn more about JIRA import execution results API.
 *
 * @author Juan Krzemien
 */
public interface Jira {

    @POST
    @Path("/rest/raven/1.0/import/execution")
    @Consumes("application/json")
    Response importResults(ExecutionResults results);

}
