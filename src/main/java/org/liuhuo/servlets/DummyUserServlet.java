package org.liuhuo.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.util.List;
import java.util.ArrayList;

public class DummyUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Resource(name="jdbc/playground")
    DataSource ds;

    public DummyUserServlet() {
	super();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String query = "select * from dummy_user";

	try (Connection conn = ds.getConnection();
	     Statement stmt = conn.createStatement();
	     ResultSet rs = stmt.executeQuery(query)) {

	    List<DummyUser> result = new ArrayList<>();
	    while (rs.next()) {
		int id = rs.getInt("id");
		String user = rs.getString("name");
		String role = rs.getString("role");
		DummyUser du = new DummyUser(id,user,role);
                System.out.println(du);
		result.add(du);
	    }
	    request.setAttribute("result",result);
	    RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/templates/dummy.ftl");
	    view.forward(request,response);
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
