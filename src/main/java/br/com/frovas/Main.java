package br.com.frovas;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {

	public static void main(String ... args) {

		Quarkus.run(args);

		System.out.println("-----------------");
		System.out.println("  INICIANDO API  ");
		System.out.println("-----------------");

	}
}