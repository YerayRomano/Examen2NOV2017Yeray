package controlador;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class Servlet
 */
//@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Servlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession sesion = request.getSession();
		if (request.getParameter("action") == null) {
			request.getRequestDispatcher("/indice.jsp").forward(request, response);
		} else {
			switch (request.getParameter("action")) {
			case "ej1":
				HashMap<String, String> losEnlaces = new HashMap<String, String>();
				losEnlaces.put("Google", "http://www.google.es");
				losEnlaces.put("Wallapop", "http://www.wallapop.com");
				losEnlaces.put("Satakore", "http://www.satakore.com");
				losEnlaces.put("vibbo", "http://www.vibbo.com");
				losEnlaces.put("youtube", "http://www.youtube.com");
				losEnlaces.put("ebay", "http://www.ebay.com");
				request.setAttribute("enlaces", losEnlaces);
				request.getRequestDispatcher("/ej1.jsp").forward(request, response);
				break;
			case "ej2":
				if (sesion.getAttribute("nombre") == null)
					request.getRequestDispatcher("/ej2_form.jsp").forward(request, response);
				else
					request.getRequestDispatcher("/saludo.jsp").forward(request, response);
			break;
			case "logout":
				sesion.invalidate();
				request.getRequestDispatcher("/indice.jsp").forward(request, response);
			break;
			default:

				break;
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession sesion = request.getSession();
		ArrayList<String> errores = new ArrayList<String>();
		if (request.getParameter("action") == null) {
			request.getRequestDispatcher("/indice.jsp").forward(request, response);
		} else {
			switch (request.getParameter("action")) {
			case "ej2":
				if (sesion.getAttribute("nombre") != null) {
					doGet(request, response);
					return;
				}
				if (request.getParameter("usr") == null) {
					errores.add("error, no se mandado el nombre de usuario");
				} else {
					if (request.getParameter("usr").equals("")) {
						errores.add("error, el nombre de usuario esta vacio");
					}
				}
				if (request.getParameter("pss") == null) {
					errores.add("error, no se mandado la contrase&ntilde;a");
				} else {
					if (request.getParameter("pss").equals("")) {
						errores.add("error, no se admiten contrase&ntilde;as vacias");
					}
				}
				if (errores.size() == 0) {
					boolean exitoso = UsuarioExiste(request.getParameter("usr"), request.getParameter("pss"));
					if(!exitoso) {
						errores.add("error, credenciales no validas");
					}
				}
				if(errores.size() == 0) {
					sesion.setAttribute("nombre", request.getParameter("usr"));
					doGet(request, response);
				} else {
					request.setAttribute("errores",errores);
					request.getRequestDispatcher("/errores.jsp").forward(request, response);
					return ;
				}
				break;
			default:
				response.sendError(405);
				break;
			}
		}
	}

	private Connection conexion() {

		String USUARIO = "root";
		String PASS = "";
		String URL_BD = "jdbc:mysql://127.0.0.1:3306/biblioteca";
		try {
			return (Connection) DriverManager.getConnection(URL_BD, USUARIO, PASS);
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof ClassNotFoundException) {
				System.err.println("VERIFIQUE Si EL DRIVER DE LA BD ESTA EN  CLASSPATH");
			} else {
				System.err.println("ESTA ARRANCANDO MYSQL ?, lAS CREDENCIALES ESTÁN BIEN ?");
			}
			System.exit(0);
			// el programa termina y devuelve el control al S.O.
			return null;
		}
	}

	private boolean UsuarioExiste(String usr, String pss) {
		String consulta = "SELECT * FROM usuarios WHERE usuario='" + usr + "' AND clave='" + pss + "'";
		try {
			Connection con = this.conexion();
			PreparedStatement preparedStatement = con.prepareStatement(consulta);
			// preparedStatement.setString(1,mail);
			// preparedStatement.setString(2,pss);
			ResultSet rs = preparedStatement.executeQuery(consulta);
			return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
