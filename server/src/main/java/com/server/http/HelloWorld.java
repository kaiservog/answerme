package com.server.http;

import static spark.Spark.get;

public class HelloWorld {
	public static void registerResource() {
		get("/hello", (req, res) -> "Hello World");
	}
}
