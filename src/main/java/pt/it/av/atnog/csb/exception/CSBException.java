package pt.it.av.atnog.csb.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import pt.it.av.atnog.csb.entity.common.ErrorResponse;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 *
 */
public class CSBException extends WebApplicationException {

    private static final long serialVersionUID = -169884243290439823L;

	public CSBException(Status errorCode, String errorMessage) {
        Response.ResponseBuilder builder = Response.status(errorCode);
        
        ErrorResponse err = new ErrorResponse();
		err.setCode(errorCode.toString());
		err.setMessage(errorMessage);
        
        builder.entity(err);
        throw new WebApplicationException(builder.build());
    }
}
