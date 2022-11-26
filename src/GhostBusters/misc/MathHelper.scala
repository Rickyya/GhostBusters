package GhostBusters.misc

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class MathHelper:
  def request() =
    val request: HttpRequest = HttpRequest.newBuilder.uri(URI.create("https://random-math-problem.p.rapidapi.com/random-problem?type=json")).header("X-RapidAPI-Key", "dJgUspUP8Imshv5wDeTFU6Cx48wgp1whu6QjsnaKSQeXUt5F5Q").header("X-RapidAPI-Host", "random-math-problem.p.rapidapi.com").method("GET", HttpRequest.BodyPublishers.noBody).build
    val response: HttpResponse[String] = HttpClient.newHttpClient.send(request, HttpResponse.BodyHandlers.ofString)
    println(response.body)

  def getMathProblem() =
    request()

end MathHelper
