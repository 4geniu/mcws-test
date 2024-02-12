package mcws;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class App {
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		String url = "ws://localhost:8080/SampleServer/WebSocketServer";
		HttpClient client = HttpClient.newHttpClient();
		WebSocket.Builder wsb = client.newWebSocketBuilder();
		// The receiving interface of WebSocket
		WebSocket.Listener listener = new WebSocket.Listener() {
			// A WebSocket has been connected.
			@Override
			public void onOpen(WebSocket webSocket) {
				System.out.println("onOpen");
				// Increments the counter of invocations of receive methods.
				webSocket.request(10);
			}
			// A textual data has been received.
			@Override
			public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
				System.out.println("onText");
				System.out.println(data);
				return null;
			}
		};
		// Builds a WebSocket connected to the given URI and associated with the given
		// Listener.
		CompletableFuture<WebSocket> comp = wsb.buildAsync( //
				URI.create(url) //
				, listener);
		// Waits if necessary for this future to complete, and then returns its result.
		WebSocket ws = comp.get();
		// Sends textual data with characters from the given character sequence.
		ws.sendText("Hello.", true);
		Thread.sleep(1000);
		// Initiates an orderly closure of this WebSocket's output by sending
		// a Close message with the given status code and the reason.
		CompletableFuture<WebSocket> end = ws.sendClose(WebSocket.NORMAL_CLOSURE, "CLOSURE");
		// Waits if necessary for this future to complete, and then returns its result.
		end.get();
	}
}


