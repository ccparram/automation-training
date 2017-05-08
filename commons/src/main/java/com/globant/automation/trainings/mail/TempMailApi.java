package com.globant.automation.trainings.mail;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

/**
 * Models Temp Mail API according to https://temp-mail.ru/en/api/
 *
 * @author Juan Krzemien
 */
public interface TempMailApi {

    @GET
    @Path("/request/domains/format/json/")
    List<String> getPossibleMailDomains();

    @GET
    @Path("/request/mail/id/{md5}/format/json/")
    List<Mail> getMail(@PathParam("md5") String emailAddressMd5CheckSum);

}
