package rawinng.hellogrpc.client

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import rawinng.hellogrpc.proto.helloRequest
import java.io.Closeable
import java.util.concurrent.TimeUnit

class HelloWorldClient(private val channel: ManagedChannel) : Closeable {
    private val stub = rawinng.hellogrpc.proto.GreeterGrpcKt.GreeterCoroutineStub(channel)

    suspend fun greet(name: String) {
        val request = helloRequest {
            this.name = name
        }
        val result = stub.sayHello(request)
        println("Receive: ${result.message}")
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

suspend fun main(args: Array<String>) {
    val port = 32098
    val channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build()
    val client = HelloWorldClient(channel)
    client.use {
        val user = args.singleOrNull() ?: "world"
        it.greet(user)
    }
}