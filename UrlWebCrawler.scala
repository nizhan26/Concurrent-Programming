object UrlWebCrawler {
	//use a mutable set to store URL whose adjacent list has been visited by workder
	val adjvisited = mutable.Set[Vertex]()
		
		//each worker repeatedly get vertex from controller, red the adj list and return
	
	def Workder(me:Int,
				adjgraph: Graph,
				tasksIn: ?[Vertex],
				tasksOut: ![Option[Vertex]],
				) = proc(s"worker $me"){
	repeat{
		//read task from tasksIn channel
		val vetex = tasksIn?()
		val adj = adjgraph.adjacent(vertex)
		adjvisited.add(vertex)
		val it = adj.iterator
		while(it.hasNext) taskOut!Some(it.next());
		tasksOut!None
		}
		tasksOut.closeOut()				
	}
	
	def Controller(start: String, tasksOut: Seq[![Vertex]], tasksIn: ?[Option[Vertex]]) = proc('Controller'){
		var busyWorkers = 0
		
		//initial task is the start url, stored in a queue
		val tasks = new scala.collection.mutable.Queue[Vertex]
		tasks.enqueue(start)
		val result = mutabble.Set[Vertex]()
		result.add(start)
			
		//repeatedly and alternatively run done by serve
		serve(
			//when the task bag is not empty, and tasksOut channel is not closed, send the task to workers
		|(for(out <- tasksOut) yield
			(!tasks.isEmpty && out) =!=>
			{
				busyWorkers += 1
				tasks.dequeue()
			}
			)
				
		|(busyWorkders>0 && tasksIn) =?=>{
			case Some(vertex) =>
			if(!result.contains(vertex)){
				result.add(vertex)
			}
			if(!adjvisited.contains(vertex)){
				tasks.enqueue(vertex)
			}
			case None => busyWorkers -= 1
		}
		)
		for(out <- tasksOut) out.closeOut()
		tasksIn.closeIn()
				
		val re = result.iterator
		var count = 0
			
		while(re.hasNext){
			count += 1
			println(re.next())
		}
		println(count + "reachable links with" + start)
	}
	
	val BUFSIZE = 100000
	def crawler(web: Graph, start:String, workers: Int){
		val fromW = N2NBuf[Option[Vertex]](BUFSIZE, workers, 1,'fromW')
		val toW = for(w<-0 until workers) yield OneOne[Vertex](s"toW($w)")
		
		val Workers:PROC =  
		||(for(w<- 0 until workers) yield Worker(w,web,toW(w),fromW))  
	  		
		(Workers || Controller(start, toW, fromW))()
			
	}
	
	def main(args: Array[String]): Unit = {
		val adjgraph = AdjacencyGraph.apply("/Users/apple/IdeaProjects/Exam/src/main/scala/OUDCS-ENWIKI.edges")
			
		val start = "https://en.wikipedia.org/wiki"
		
		crawler(adjgraph, start, 1000)
	}
}
