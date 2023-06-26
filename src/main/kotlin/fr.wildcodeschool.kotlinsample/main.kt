package fr.wildcodeschool.kotlinsample


import kotlinx.coroutines.*


fun main() = runBlocking<Unit>{
    val service = SpaceXApi()
    val launches: List<RocketLaunch> = service.getAllLaunches()

/*    var i = 0
    for (l in launches) {
        println("Launch $i : ${l}")
        i++
    }*/
/*    for (l in launches) {
        async {
            println("download article for launch ${l.flightNumber}")
            service.downloadArticle(l)
            println("DONE download article for launch ${l.flightNumber}")
        }
    }*/
    val jobs = launches.map { launch ->
        async {
            println("download article for launch ${launch.flightNumber}")
            service.downloadArticle(launch)
            println("DONE download article for launch ${launch.flightNumber}")
        }
    }

    jobs.awaitAll()
    println("All articles downloaded.")
}