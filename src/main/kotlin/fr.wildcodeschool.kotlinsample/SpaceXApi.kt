package fr.wildcodeschool.kotlinsample

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json
import java.nio.file.Files

class SpaceXApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun getAllLaunches(): List<RocketLaunch> {
        return httpClient.get("https://api.spacexdata.com/v5/launches").body()
    }

    private val targetDir = Files.createTempDirectory("spacex-articles")
    suspend fun downloadArticle(launch: RocketLaunch) {
        launch.links.article?.let { articleUrl ->
            val url = Url(articleUrl)

            val targetPath = targetDir.resolve("article_" + launch.flightNumber +".html")
            println("download to " + targetPath)
            httpClient.get(url).bodyAsChannel().copyAndClose(targetPath.toFile().writeChannel())
        }
    }
}