package myservice.mynamespace.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sap.olingo.jpa.metadata.api.JPAEdmProvider;
import com.sap.olingo.jpa.metadata.api.JPAEntityManagerFactory;
import myservice.mynamespace.data.Storage;
import myservice.mynamespace.service.DemoEdmProvider;
import myservice.mynamespace.service.DemoEntityCollectionProcessor;

import myservice.mynamespace.service.DemoEntityProcessor;
import myservice.mynamespace.service.DemoPrimitiveProcessor;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.*;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.commons.api.edmx.EdmxReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a standard HttpServlet implementation.
 * It is used as main entry point for the web application that carries the OData service.
 * The implementation of this HttpServlet simply delegates the user requests to the ODataHttpHandler
 */
public class DemoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(DemoServlet.class);

    private static final String PUNIT_NAME = "PetStore";

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

        try {
//            HttpSession session = req.getSession(true);
//            Storage storage = (Storage) session.getAttribute(Storage.class.getName());
//            if (storage == null) {
//                storage = new Storage();
//                session.setAttribute(Storage.class.getName(), storage);
//            }

            EntityManagerFactory emf = JPAEntityManagerFactory.getEntityManagerFactory(PUNIT_NAME, new HashMap<String, Object>());
            JPAEdmProvider metadataProvider = new JPAEdmProvider(PUNIT_NAME, emf, null, new String[]{"myservice.mynamespace"});

            // create odata handler and configure it with EdmProvider and Processor
            OData odata = OData.newInstance();
            ServiceMetadata edm = odata.createServiceMetadata(metadataProvider, new ArrayList<EdmxReference>());
            ODataHttpHandler handler = odata.createHandler(edm);
//            handler.register(new DemoEntityCollectionProcessor(storage));
//            handler.register(new DemoEntityProcessor(storage));
//            handler.register(new DemoPrimitiveProcessor(storage));

            // let the handler do the work
            handler.process(req, resp);

        } catch (RuntimeException | ODataException e) {
            LOG.error("Server Error occurred in ExampleServlet", e);
            throw new ServletException(e);
        }
    }
}
