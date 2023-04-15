import java.awt.event.ActionEvent
import java.awt.{BorderLayout, GridBagLayout}
import javax.swing._
import java.sql.{Connection, DriverManager, ResultSet}

object Main extends App {
  val carList = new JList[String](Array[String]())

  SwingUtilities.invokeLater(new Runnable() {
    override def run(): Unit = {
      createAndShowGUI()
    }
  })

  def createAndShowGUI(): Unit = {
    val frame = new JFrame("Car Repository")
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setSize(500, 300)
    frame.setLocationRelativeTo(null)
    frame.setVisible(true)

    val contentPane = frame.getContentPane()
    contentPane.setLayout(new BorderLayout())

    val scrollPane = new JScrollPane(carList)
    contentPane.add(scrollPane, BorderLayout.CENTER)

    val buttonPanel = new JPanel(new GridBagLayout())
    val addButton = new JButton("Add Car")
    val deleteButton = new JButton("Delete Car")

    buttonPanel.add(addButton)
    buttonPanel.add(deleteButton)
    contentPane.add(buttonPanel, BorderLayout.SOUTH)

    updateCarList()

    addButton.addActionListener((_: ActionEvent) => {
      val brand = JOptionPane.showInputDialog(frame, "Enter the brand name:")
      val name = JOptionPane.showInputDialog(frame, "Enter the car's name:")
      val photo = JOptionPane.showInputDialog(frame, "Enter the car's photo:")

      saveCar(brand, name, photo)
      updateCarList()
    })

    deleteButton.addActionListener((_: ActionEvent) => {
      val selectedCar = carList.getSelectedValue()
      
      if (selectedCar != null) {
        var regex = """#(\d+)""".r
        var carId = regex.findFirstMatchIn(selectedCar).map(_.group(1).toInt)
        deleteCar(carId.getOrElse(0))
        updateCarList()
      }
    })
  }

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

  def loadCars(): Seq[String] = {
    val connection = connect()

    try {
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM cars")
      var cars: Seq[String] = Seq()

      while (resultSet.next()) {
        val id = resultSet.getInt("id")
        val brand = resultSet.getString("brand")
        val name = resultSet.getString("name")
        val photo = resultSet.getString("photo")

        cars = cars :+ s"#$id: $brand $name"
      }

      return cars
    } catch {
      case e: Exception => e.printStackTrace
      return Seq()
    } finally {
      connection.close()
    }
  }

  def deleteCar(id: Int) = {
    val connection = connect()

    try {
      val statement = connection.prepareStatement("DELETE FROM cars WHERE id = ?")
      statement.setInt(1, id)
      statement.executeUpdate()
    } catch {
      case e: Exception => e.printStackTrace
    } finally {
      connection.close()
    }
  }

  def updateCarList() = {
    val cars = loadCars()
    carList.setListData(cars.toArray)
  }

  while (true) {
    Thread.sleep(1000)
  }
}