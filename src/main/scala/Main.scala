import java.sql.{Connection, DriverManager, ResultSet}

object Main extends App {

  def connect(): Connection = {
    val url = "jdbc:mysql://localhost:3306/scala"
    val username = "root"
    val password = ""
    var connection: Connection = null

    Class.forName("com.mysql.jdbc.Driver")
    connection = DriverManager.getConnection(url, username, password)

    return(connection)
  }

  def saveUser(firstName: String, lastName: String, age: Int) = {
    val connection = connect

    try {
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

  def loadUsers(): Unit = {
    val connection = connect

    try {
      // Query all the users from the table
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM users")

      // Print the users
      while (resultSet.next()) {
        val id = resultSet.getInt("id")
        val firstName = resultSet.getString("first_name")
        val lastName = resultSet.getString("last_name")
        val age = resultSet.getInt("age")

        println(s"#$id: $firstName $lastName is $age years old.")
      }

      println("")
    } catch {
      case e: Exception => e.printStackTrace
    } finally {
      connection.close()
    }
  }

  def createUser() = {
    // Ask the user to enter their first name
    print("Enter your first name: ")
    val firstName = scala.io.StdIn.readLine()

    // Ask the user to enter their last name
    print("Enter your last name: ")
    val lastName = scala.io.StdIn.readLine()

    // Ask the user to enter their age
    print("Enter your age: ")
    val age = scala.io.StdIn.readInt()

    // Saving the user
    saveUser(firstName, lastName, age)
  }

  def showMenu(): Int = {
    println("[ User Repository ]")
    println("1 - Add user")
    println("2 - Show users")
    println("3 - Quit")
    print("> ")

    return(scala.io.StdIn.readInt())
  }

  // Option definition
  var option = 0;

  do {
    option = showMenu

    if (option == 1) {
      createUser()
    }

    if (option == 2) {
      loadUsers()
    }

    print(option)
  } while (option != 3)
}