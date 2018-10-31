import java.awt._

import io.threadcso._

/*
1. Any live cell with fewer than two live neighbours dies, as if by needs caused by underpopulation.
2. Any live cell with more than three live neighbours dies, as if by overcrowding.
3. Any live cell with two or three live neighbours lives, un- changed, to the next generation.
4. Any tile with exactly three live neighbours cells will be pop- ulated with a living cell.
*/
class Display(N: Int, a:Array[Array[Boolean]]) extends Frame {
  // Define some constants
  private val blockSize = 6
  private val padding = 1
  private val gridSize = blockSize + 2 * padding

  // Set up the display
  private val pane = new ScrollPane()
  pane.setSize(N * gridSize, N * gridSize)
  private val board = new Board()
  pane.add(board)
  this.add(pane, "Center")
  this.pack()
  this.setVisible(true)
  this.setTitle("Life")
  this.setSize(N * gridSize, N * gridSize)

  // Fill in all the squares
  def draw = {
    for (i <- 0 until N) {
      for (j <- 0 until N) {
        if (a(i)(j)) board.drawAt(j, i) else board.blankAt(j, i)
      }
    }
  }

  override def paint(g: Graphics) = draw

  class Board extends Component {
    // Define colours
    val backgroundColor = Color.gray.brighter
    val blockColor = Color.black

    // Paint the square at (x,y) in colour c
    private def paintAt(x: Int, y: Int, c: Color) = {
      val g = getGraphics()
      g.setColor(c)
      g.fillRect(x * gridSize + padding, y * gridSize + padding, blockSize, blockSize)
    }

    // Draw a piece
    def drawAt(x: Int, y: Int) = paintAt(x, y, blockColor)

    // Draw a blank square
    def blankAt(x: Int, y: Int) = paintAt(x, y, backgroundColor)
  }

  //return a object grid
}



object GameOfLife {
  //input the seed
  /*//Standard Game of Life:B3/S23
  val N = 5
  val grid = Array.ofDim[Boolean](N,N)
  grid(1)(2)=true
  grid(2)(2)=true
  grid(3)(2)=true*/

  //Highlife:B36/S23
  val N = 15
  val grid = Array.ofDim[Boolean](N,N)
  grid(5)(10)=true;grid(5)(11)=true;grid(5)(12)=true
  grid(6)(9)=true;grid(6)(12)=true
  grid(7)(8)=true;grid(7)(12)=true
  grid(8)(8)=true;grid(8)(11)=true
  grid(9)(8)=true;grid(9)(9)=true;grid(9)(10)=true

    val WORKERS = 4
    val barrier = new Barrier(WORKERS+1)

  def takeTime(t:Nanoseconds):Unit = {
    val deadline = nanoTime + t
    val ahead = deadline - nanoTime
    barrier.sync()
    if (ahead >0 ) sleep(ahead)
    barrier.sync()
  }

  def displayChart = proc{
    val display = new Display(N,grid)
    barrier.sync()
		//draw first and workers take rest together with barrier
    while (true){
      display.draw
      takeTime(seconds(0.3))
    }
  }

    def workers (sr:Int,er:Int,sc:Int,ec:Int)= proc {
		//define a local board for each worker
      val local = Array.ofDim[Boolean](er - sr + 1, ec - sc + 1)
		  //is ready when the funciton is called the times of the workers
      barrier.sync()
      while (true) {
        for (row <- sr until er+1; column <- sc until ec+1) {
          val numberOfAliveNeighbors = numberOfAliveNeighborsOfCellAt(row,column)
          //Standard Game of Life:B3/S23
          /*if (grid(row)(column)) {
            if (numberOfAliveNeighbors < 2) local(row - sr)(column - sc) = false
            else if (numberOfAliveNeighbors > 3) local(row - sr)(column - sc) = false
            else local(row - sr)(column - sc)=true
          } else if (numberOfAliveNeighbors == 3) local(row - sr)(column - sc) = true
            */
          //Highlife:B36/S23
          if (grid(row)(column)) {
            if (numberOfAliveNeighbors < 2) local(row - sr)(column - sc) = false
            else if (numberOfAliveNeighbors > 3) local(row - sr)(column - sc) = false
            else local(row - sr)(column - sc)=true
          } else if (numberOfAliveNeighbors == 3 || numberOfAliveNeighbors == 6) local(row - sr)(column - sc) = true

}

barrier.sync()
for (row <- sr until er+1; column <- sc until ec+1) {
grid(row)(column) = local(row - sr)(column - sc)
//println(f"$row%s $column%s"+ " "+ grid(row)(column))
}
barrier.sync()
}
}

def numberOfAliveNeighborsOfCellAt(x: Int, y: Int): Int = {
var numberOfAliveNeighbors: Int = 0
def cellExists(x: Int, y: Int): Boolean = {
if (x >= 0 && x <= (N - 1)&& y >= 0 && y <= (N - 1))
return true
else return false
}

  if (cellExists(x - 1, y - 1)) {
  if(grid(x-1)(y-1))
  numberOfAliveNeighbors += 1
  }else if(grid((x-1)%N)((y-1)%N)) {numberOfAliveNeighbors+=1}

  if (cellExists(x - 1, y)) {
    if(grid(x-1)(y))
      numberOfAliveNeighbors += 1
  }else if(grid((x-1)%N)(y%N)) {numberOfAliveNeighbors+=1}

  if (cellExists(x - 1, y + 1)) {
    if(grid(x-1)(y+1))
      numberOfAliveNeighbors += 1
  }else if(grid((x-1)%N)((y+1)%N)) {numberOfAliveNeighbors+=1}

  if (cellExists(x, y - 1)) {
    if(grid(x)(y-1))
      numberOfAliveNeighbors += 1
  }else if(grid(x%N)((y-1)%N)) {numberOfAliveNeighbors+=1}

  if (cellExists(x, y + 1)) {
    if(grid(x)(y+1))
      numberOfAliveNeighbors += 1
  }else if(grid(x%N)((y+1)%N)) {numberOfAliveNeighbors+=1}

  if (cellExists(x + 1, y - 1)) {
    if(grid(x+1)(y-1))
      numberOfAliveNeighbors += 1
  }else if(grid((x+1)%N)((y-1)%N)) {numberOfAliveNeighbors+=1}

  if (cellExists(x + 1, y)) {
    if(grid(x+1)(y))
      numberOfAliveNeighbors += 1
  }else if(grid((x+1)%N)(y%N)) {numberOfAliveNeighbors+=1}

  if (cellExists(x + 1, y + 1)) {
    if(grid(x+1)(y+1))
      numberOfAliveNeighbors += 1
  }else if(grid((x+1)%N)((y+1)%N)) {numberOfAliveNeighbors+=1}


return numberOfAliveNeighbors
}





//val System = workers(0,2,0,2)||workers(3,4,0,2)||workers(0,2,3,4)||workers(3,4,3,4)||displayChart
  val System = workers(0,7,0,7)||workers(8,14,0,7)||workers(0,7,8,14)||workers(8,14,8,14)||displayChart

def main(args: Array[String]): Unit = {

System()
}
}










