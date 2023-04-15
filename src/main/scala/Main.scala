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

  def saveCar(brand: String, name: String, photo: String) = {
    val connection = connect

    try {
      // Insert the cars's brand, name and photo into a table
      val statement = connection.prepareStatement("INSERT INTO cars (brand, name, photo) VALUES (?, ?, ?)")

      statement.setString(1, brand)
      statement.setString(2, name)
      statement.setString(3, photo)
      statement.executeUpdate()
    } catch {
      case e: Exception => e.printStackTrace
    } finally {
      connection.close()
    }
  }

  def loadCars(): Unit = {
    val connection = connect

    try {
      // Query all the cars from the table
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM cars")

      // Print the cars
      while (resultSet.next()) {
        val id = resultSet.getInt("id")
        val brand = resultSet.getString("brand")
        val name = resultSet.getString("name")
        val photo = resultSet.getString("photo")

        println(s"#$id: $brand $name")
      }

      println("")
    } catch {
      case e: Exception => e.printStackTrace
    } finally {
      connection.close()
    }
  }

  def createCar() = {
    
    // Ask the user to enter the car's brand
    print("Enter the brand name: ")
    val brand = scala.io.StdIn.readLine()

    // Ask the user to enter the car's name
    print("Enter the car's name: ")
    val name = scala.io.StdIn.readLine()

    // Ask the user to enter the car's photo
    print("Enter the car's photo: ")
    val photo = scala.io.StdIn.readLine()

    // Saving the car
    saveCar(brand, name, photo)
  }

  def showMenu(): Int = {
    println("[ Car Repository ]")
    println("1 - Show all cars")
    println("2 - Add a new car")
    println("3 - Quit")
    print("> ")

    return(scala.io.StdIn.readInt())
  }

  // Option definition
  var option = 0;

  do {
    option = showMenu

    if (option == 1) {
      loadCars()

    }
    if (option == 2) {
      createCar()
    }

    print(option)
  } while (option != 3)
}