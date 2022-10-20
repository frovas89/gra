package br.com.frovas;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {

	public static void main(String ... args) {
		System.out.println("----------------------------------");
		System.out.println("  INICIANDO IMPORTAÇÃO DOS DADOS  ");
		System.out.println("----------------------------------");
		Quarkus.run(args);
	}
}