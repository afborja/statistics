package com.andresborja.stats.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.andresborja.stats.statscollector.State;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller server for transactions requests.
 * @author afborja
 */
public class StatisticsServlet extends StatsAbstractServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        State state = super.getState();
        ObjectMapper mapper = super.getMapper();
        String stateAsJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(state);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(stateAsJSON);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Statistics controller</h1>");
        response.getWriter().println("Only GET method supported");
    }

}
