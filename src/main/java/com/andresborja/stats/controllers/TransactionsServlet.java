package com.andresborja.stats.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.andresborja.stats.statscollector.InvalidTransactionTimeException;
import com.andresborja.stats.statscollector.State;
import com.andresborja.stats.statscollector.Transaction;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

/**
 * Controller server for transactions requests.
 * @author afborja
 */
public class TransactionsServlet extends StatsAbstractServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        State state = getState();

        JsonFactory factory = new JsonFactory();
        JsonParser parser  = factory.createParser(request.getReader());

        Transaction t = getMapper().reader().readValue(parser, Transaction.class);

        int requestStatus = 201;
        try {
            state.add(t);
        } catch (InvalidTransactionTimeException e) {
            requestStatus = 204;
        }

        response.setStatus(requestStatus);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Transactions Controller</h1>");
        response.getWriter().println("Only POST method supported");
    }

}
