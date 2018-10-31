import io.threadcso._

/**
        A solution to the problem will extend this class, which simply
        provides a standard test-rig.
*/

trait SleepingTutor
{
  /** Protocol for tutor to wait for students to arrive */
  def TutorWait: Unit
  /** Protocol for student to arrive and wait for tutorial */
  def Arrive: Unit
  /** Protocol for students to receive tutorial */
  def ReceiveTute: Unit
  /** Protocol for tutor to end tutorial */
  def EndTeach: Unit

  val start = nanoTime
  def now   = (nanoTime-start)/1E9 // in seconds
  def log(me: String, item: String) =
      println(f"$now%6.5f $me%-5s $item%s")

  val random = new scala.util.Random

  def Student(me: String) = proc(s"Student $me") {
    while (true) {
      sleep(random.nextInt(3)*Sec)
      log(me, "arrives"); Arrive
      log(me, "ready");   ReceiveTute
      log(me, "leaves")
    }
  }

  def Tutor = proc("Tutor") {
    while (true) {
      log("Tutor", "waiting");   TutorWait
      log("Tutorial", "starts");
      sleep(random.nextInt(3)*Sec)
      log("Tutorial", "ends");   EndTeach;
      sleep(random.nextInt(3)*Sec)
    }
  }

  val System = Tutor || Student("Ada") || Student("Bob")

  def main(args: Array[String]) =   
  System()
}



//Monitors
object ChaoticTutor extends SleepingTutor
{
private var student=0
private var tu_end,tu_awake=false


  def TutorWait         = synchronized{
        while(!tu_awake) wait()
}

  def Arrive            = synchronized{
        student+=1
        while(student!=2) wait()
        tu_awake=true
        notifyAll
}
  def ReceiveTute       = synchronized{
        while(!tu_end) wait()

        student-=1
        while(student!=0) wait()
        notifyAll

        tu_end=false
}
  def EndTeach          = synchronized{
        tu_end=true
        notifyAll
        tu_awake=false
        }
}
//Semaphores

object SemaTutor extends SleepingTutor
{
  private var student:Int = 0
  private var count = 0
  private val mutex=BooleanSemaphore(available=false)
  private val tutor_awake=BooleanSemaphore(available = false)
  private val tu_end=BooleanSemaphore(available = false)
  val gate = BooleanSemaphore(available = false)

  def TutorWait         = {
    tutor_awake.acquire
  }

  def Arrive            = {
    mutex.acquire
    student+=1
    if(student==2)
    {
      gate.release
      tutor_awake.release
      mutex.release
      count+=1
      println(count)
    }
    else
    {
      mutex.release
      gate.acquire
    }
  }
  def ReceiveTute       = {
    mutex.acquire

    student-=1
    if(student==0){
      tu_end.acquire
      gate.release
      mutex.release
    }
    else{
      mutex.release
      gate.acquire
    }
  }
  def EndTeach          = {
    tu_end.release
  }

}