package rawinng.hellogrpc.server

import io.grpc.Server
import io.grpc.ServerBuilder
import rawin.hellogrpc.*

class HelloWorldServer(private val port: Int) {
    private val server: Server = ServerBuilder.forPort(port)
        .addService(HelloWorldService())
        .build()

    fun start() {
        server.start()
        println("Server started, listing to $port")
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down grpc ***")
                this@HelloWorldServer.stop()
                println("*** grpc stop ***")
            }
        )
    }
    private fun stop() {
        server.shutdown()
    }
    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    internal class HelloWorldService : GreeterGrpcKt.GreeterCoroutineImplBase() {
        override suspend fun sayHello(request: HelloRequest) = helloReply {
            message = "Hello ${request.name}"
        }
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50051
    val server = HelloWorldServer(port)
    server.start()
    server.blockUntilShutdown()
}