package com.google.appengine.codelab;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;

/**
 * The class fetches the orders page by page.
 * 
 * @author
 * 
 */
@SuppressWarnings("serial")
public class PaginationServlet extends HttpServlet {

  public static final int PAGE_SIZE = 10;

  @SuppressWarnings("deprecation")
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    resp.setContentType("text/html");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    PrintWriter writer = resp.getWriter();
    PreparedQuery queryString = datastore.prepare(new Query("order"));
    
    int rowCount = queryString.countEntities();
    
    // set offset for the results to be fetched
    String off = req.getParameter("offset");
    int offset = 0;
    if (off != null)
      offset = Integer.parseInt(off);
    
    // fetch results from datastore based on offset and page size
    FetchOptions fetchOptions = FetchOptions.Builder.withLimit(PAGE_SIZE);
    String pageContent = "<tbody>";

    QueryResultList<Entity> results = queryString
        .asQueryResultList(fetchOptions.offset(offset));   
    
    // set the header content
    StringBuilder header = new StringBuilder("<thead><tr>"
        + "<th scope=\"col\">Order Id</th>"
        + "<th scope=\"col\">Item Name</th>"
        + "<th scope=\"col\">Customer</th>" + "<th scope=\"col\">Quantity</th>"
        + "<th scope=\"col\">Price</th>" + "<th scope=\"col\">Status</th>"
        + "<th scope=\"col\">Mark</th>" + "</tr></thead>");
    writer.println(header);
    
    // set footer for the table
    float pageCount = (float) rowCount / PAGE_SIZE;
    String footer = "";
    if (pageCount > rowCount / PAGE_SIZE)
      pageCount = (int) pageCount + 1;
    else
      pageCount = (int) pageCount;

    for (int i = 0; i < pageCount; i++) {
      footer += "<a href=\"#\" onclick=\"fillBody(" + i * PAGE_SIZE + ")\">"
          + (i + 1) + "</a>  ";
    }

    if (rowCount > 10) {
      if ((offset / 10) != (pageCount - 1))
        footer += "<a href=\"#\" onclick=\"fillBody(" + (offset + 10)
            + ")\">Next</a>";
      else
        footer += "<a href=\"#\" onclick=\"fillBody(" + (pageCount - 1)
            * PAGE_SIZE + ")\">Next</a>";
    }
    writer.println("<tfoot><tr><td colspan=7 class=\"id\" style=\"text-align:right\">"
            + footer + "</td></tr></tfoot>");
    
    // set the content of the table
    for (Entity entity : results) {
      pageContent += "<tr>";
      pageContent += "<td>" + entity.getKey().getId() + "</td><td>"
          + entity.getProperty("itemName") + "</td><td>"
          + entity.getProperty("customerName") + "</td><td>"
          + entity.getProperty("quantity") + "</td><td>"
          + entity.getProperty("price") + "</td><td>"
          + entity.getProperty("status") + "</td>";
      if (entity.getProperty("status").toString()
          .equalsIgnoreCase("Processing")
          || entity.getProperty("status").toString()
              .equalsIgnoreCase("processed")) {
        pageContent += "<td><INPUT id=\"orders\" NAME=\"orders\" TYPE=\"CHECKBOX\" DISABLED VALUE="
            + entity.getKey().getId() + "></td></tr>";
      } else {
        pageContent += "<td><INPUT id=\"orders\" NAME=\"orders\" TYPE=\"CHECKBOX\" VALUE="
            + entity.getKey().getId() + "></td></tr>";
      }
    }
    if (results.isEmpty()) {
      // condition to show message when data is not available
      pageContent += "<tbody><tr><td colspan=7>No records found</td></tr>";
    }
    pageContent += "</tbody>";
    writer.println(pageContent);
  }
}