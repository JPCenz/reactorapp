package com.jpcenzano.reactorapp;

import com.jpcenzano.reactorapp.model.Usuario;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.LinkedList;

@SpringBootApplication
public class ReactorappApplication implements CommandLineRunner {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ReactorappApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ReactorappApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Ejemplo de uso de Flux con Reactor 3 >>");
        Flux<String> nombres = Flux.just("Javier", "Pedro", "Pepe", "", "Ana", "Luis")
                .doOnNext(e -> {
                    if (e.isEmpty()) {
                        throw new RuntimeException("No puede ser vacio");
                    }
                    log.info("DoOnNext() "+e);
                });
        nombres.subscribe(e -> log.info("Suscribe() "+e), error -> log.error(error.getMessage()), new Runnable() {
            @Override
            public void run() {
                log.info("Ha finalizado la ejecucion del observable con exito");
            }
        });

        //ejemploMap();
        //ejemploFilter();
        ejemploListtoObservable();

    }

    private void ejemploMap() {
        log.info("## Ejemplo Map >>");
        Flux<Usuario> nombres = Flux.just("Javier", "Pedro", "Maria", "Ana", "Luis")
                .map(nombre -> new Usuario(nombre.toUpperCase(), null))
                .doOnNext(usuario -> {
                    if (usuario== null) {
                        throw new RuntimeException("No puede ser vacio");
                    }
                    log.info("DoOnNext() "+usuario);
                })
        .map(usuario -> {
            var n = usuario.getNombre().toLowerCase();
            usuario.setNombre(n);
            return usuario;
        });

        nombres.subscribe(e -> log.info("Suscribe() "+e), error -> log.error(error.getMessage()), new Runnable() {
            @Override
            public void run() {
                log.info("Ha finalizado la ejecucion del observable con exito");
            }
        });
    }
    private void ejemploFilter() {
        log.info("## Ejemplo Filter >>");
        log.info("## Los Observables son inmutables>>");
        Flux<Usuario> nombres = Flux.just("Javier Sutano", "Pedro Pe", "Maria Magdalena", "Ana Jesus", "Pedro Perez")
                .map(nombre -> new Usuario(nombre.split(" ")[0], nombre.split(" ")[1]))
                .filter(usuario -> usuario.getNombre().equalsIgnoreCase("MAria"))
                .doOnNext(usuario -> {
                    if (usuario== null) {
                        throw new RuntimeException("No puede ser vacio");
                    }
                    log.info("DoOnNext() "+ usuario);
                });

        nombres.subscribe(e -> log.info("Suscribe() "+e), error -> log.error(error.getMessage()), new Runnable() {
            @Override
            public void run() {
                log.info("Ha finalizado la ejecucion del observable con exito");
            }
        });
    }
    private void ejemploListtoObservable() {
        log.info("## Ejemplo List to Observable >>");
        //create a list of names and last names and convert it to an observable
        var list = new LinkedList<String>();
        list.add("Javier Cenzano");
        list.add("Pedro Perez");
        list.add("Maria Magdalena");
        list.add("Ana Jesus");
        list.add("Bruce Lee");
        Flux<String> nombres = Flux.fromIterable(list);

        Flux<Usuario> usuarioFlux = nombres.map(nombre -> new Usuario(nombre.split(" ")[0], nombre.split(" ")[1]))
                .doOnNext(usuario -> {
                    if (usuario== null) {
                        throw new RuntimeException("No puede ser vacio");
                    }
                    log.info("DoOnNext() "+usuario);
                });

        usuarioFlux.subscribe(e -> log.info("Suscribe() "+e), error -> log.error(error.getMessage()), new Runnable() {
            @Override
            public void run() {
                log.info("Ha finalizado la ejecucion del observable con exito");
            }
        });
    }
}
