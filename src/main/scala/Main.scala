import java.sql.{Connection, DriverManager, ResultSet}

object Main extends App {
        
  // Ask the user to enter their first name
  print("Enter your first name: ")
  val firstName = scala.io.StdIn.readLine()

  // Ask the user to enter their last name
  print("Enter your last name: ")
  val lastName = scala.io.StdIn.readLine()

  // Ask the user to enter their age
  print("Enter your age: ")
  val age = scala.io.StdIn.readInt()

  // Connect to the database
  val url = "jdbc:mysql://localhost:3306/scala"
  val username = "root"
  val password = ""
  var connection: Connection = null

  try {
    Class.forName("com.mysql.jdbc.Driver")
    connection = DriverManager.getConnection(url, username, password)

    // Insert the user's name and age into a table
    val statement = connection.prepareStatement("INSERT INTO users (first_name, last_name, age) VALUES (?, ?, ?)")
    statement.setString(1, firstName)
    statement.setString(2, lastName)
    statement.setInt(3, age)
    statement.executeUpdate()
  } catch {
    case e: Exception => e.printStackTrace
  } finally {
    connection.close()
  }
}