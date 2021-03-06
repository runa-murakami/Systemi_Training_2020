package bbs;

import static bbs.util.CloseableUtil.*;
import static bbs.util.DBUtil.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/changeStatus"})
public class ChangeStatusServlet extends HttpServlet {
		private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		Connection connection = null;
    	PreparedStatement ps = null;
    	int count = 0;
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://192.168.2.6:3306/test";
		String user = "testuser";
		String password = "test";

		String account = request.getParameter("account");
		int status = Integer.parseInt(request.getParameter("status"));

		try {
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
	      	//SQL文をDBに送るためのいれもの
	      	connection = DriverManager.getConnection(url,user,password);
	      	connection.setAutoCommit(false);
	      	StringBuilder sql = new StringBuilder();
	      	sql.append("UPDATE users SET ");

	      	if (status == 0) {
	      		sql.append("status=1 ");
	      	} else {
	      		sql.append("status=0 ");
	      	}

	      	sql.append("WHERE account='");
	      	sql.append(account);
	      	sql.append("'");

	      	System.out.println(sql.toString());
	      	ps = connection.prepareStatement(sql.toString());
	      	count = ps.executeUpdate();

	      	System.out.println(count);

	      	commit(connection);



	} catch (RuntimeException | SQLException e) {
		rollback(connection);
	} catch (Error e) {
		rollback(connection);
		throw e;
	}
		close(connection);

//		request.getRequestDispatcher("/userManagement").forward(request, response);

		response.sendRedirect("/bbs/userManagement");
	}

}
